package hr.math.android.signme;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DrawingFragment extends Fragment {

    private final static String TAG = "DrawingFragment";
    private int number_of_signatures = 1;
    private int number_of_drawings = 3;
    private int student_id;
    private int lecture_id;
    private boolean learning_new_signature;
    private DrawingView drawing_view;
    private TextView text;

    private DBDistances dbDistances;
    private DBSignatures dbSignatures;


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
        dbSignatures = new DBSignatures(getContext());
        dbDistances = new DBDistances(getContext());

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
                    //int result = drawing_view.saveSignature(number_of_signatures, student_id, lecture_id);
                    if (saveSignature() > 0)
                        updateNumberOfSignatures();
                }
                // If we are checking signature against already known signature in database
                else {
                    Toast.makeText(getActivity(), "Checking current signature", Toast.LENGTH_LONG).show();
                    float signature_result = checkSignature();

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
        Log.d(TAG, "Too few coordinates. Number of coordinates must be over " + DBAdapter.MIN_NUM_OF_COORDS
                + " and we have " + drawing_view.x_coord.size());
        Toast.makeText(getContext(), "Try to sign slower or choose longer signature ;).\nYou are too fast.", Toast.LENGTH_LONG).show();
    }

    private void signatureTooLong() {
        Log.d(TAG, "Too many coordinates. Number of coordinates must be less than " + DBAdapter.MAX_NUM_OF_COORDS
                + " and we have " + drawing_view.x_coord.size());
        Toast.makeText(getContext(), "Try to sign faster ;).\nYou are too slow.", Toast.LENGTH_LONG).show();
    }

    private int checkCoordinatesInfo() {
        if (drawing_view.x_coord.size() > DBAdapter.MAX_NUM_OF_COORDS ) {
            signatureTooLong();
            return -1;
        } else if(drawing_view.x_coord.size() < DBAdapter.MIN_NUM_OF_COORDS ) {
            signatureTooShort();
            return -2;
        } else if(drawing_view.x_coord.size() != drawing_view.y_coord.size() ||
                drawing_view.x_coord.size() != drawing_view.pen_start.size()) {
            Log.d(TAG, "Uneven number of coordinates for x, y and pen.");
            return -3;
        } else
            return 1;
    }

    private int saveSignature() {


        // Checking if we have too many, too few or error in number of coordinates
        int result = checkCoordinatesInfo();
        if (result < 0) {
            drawing_view.discardSignature();
            return result;
        }

        Log.d(TAG, "x coord = " + drawing_view.x_coord.toString());
        Log.d(TAG, "y coord = " + drawing_view.y_coord.toString());
        Log.d(TAG, "pen start = " + drawing_view.pen_start.toString());
        Log.d(TAG, "Saving signature number " + number_of_signatures + " of student "
                + student_id + " on lecture " + lecture_id);
        Log.d(TAG, "duljina prije spremanja je: " + drawing_view.x_coord.size() + " "
                + drawing_view.y_coord.size() +" " + drawing_view.pen_start.size() );

        dbDistances.open();
        dbSignatures.open();

        Cursor c = dbSignatures.getStudentSignature(student_id, lecture_id);
        Log.d(TAG, "Got " + c.getCount() + " rows.");

        // Error in getting data from database.
        if(c.getCount() % 3 != 0) {
            //TODO: check if that can happen (e.g. phone is restarted), what to do?? maybe delete that user from database?
            Log.d(TAG, "Number of rows in database not mod 3. Some strange error.");
            return -4;
        }

        ArrayList<Float> x_normalised = DTW.normaliseXData(drawing_view.x_coord);
        ArrayList<Float> y_normalised = DTW.normaliseYData(drawing_view.y_coord);

        dbSignatures.saveSignature(number_of_signatures, student_id, lecture_id, x_normalised, y_normalised, drawing_view.pen_start);

//        float max1 = 0, max_temp1;
//        float max2 = 0, max_temp2;
        float max_temp0, max_temp1;
        ArrayList<Float> max = new ArrayList<>(2);
        max.add(0F);
        max.add(0F);

        // In database we save signatures by number from last.
        for (int i = 1; i <= c.getCount()/3; i++) {
            ArrayList<Float> x = getArray(c, "x", i);
            ArrayList<Float> y = getArray(c, "y", i);
            ArrayList<Float> p = getArray(c, "p", i);
            Log.d(TAG, "x koordinate iz baze" + x.toString());
            Log.d(TAG, "y koordinate iz baze" + y.toString());
            Log.d(TAG, "pen koordinate iz baze" + p.toString());
            Log.d(TAG, "x koordinate trenutni potpis" + x_normalised.toString());
            Log.d(TAG, "y koordinate trenutni potpis" + y_normalised.toString());
            Log.d(TAG, "pen koordinate trenutni potpis" + drawing_view.pen_start.toString());

            Log.d(TAG, "duljina iz baze je: " + x.size() + " " + y.size() +" " + p.size() );
            Log.d(TAG, "duljina live je: " + x_normalised.size() + " " + y_normalised.size() +" " + drawing_view.pen_start.size() );


            max_temp0 = DTW.calculateDistance1(x_normalised, y_normalised, drawing_view.pen_start, x, y, p);
            max_temp1 = DTW.calculateDistance2(x_normalised, y_normalised, drawing_view.pen_start, x, y, p);
            Log.d(TAG, "Maximal distance between signatures is: " + max_temp0 +
                    "maximal distance2 is " + max_temp1);
            if (max_temp0 > max.get(0))
                max.set(0, max_temp0);
            if (max_temp1 > max.get(1))
                max.set(1, max_temp1);
        }

        if(c.getCount() != 0)
            dbDistances.updateMaxDistance(student_id, lecture_id, max);
        dbDistances.close();
        dbSignatures.close();
        drawing_view.discardSignature();

        return 1;
    }


    private float checkSignature()
    {

        // Checking if we have too many, too few or error in number of coordinates
        int result = checkCoordinatesInfo();
        if (result < 0) {
            drawing_view.discardSignature();
            return result;
        }

        float min1, min2;
        ArrayList<Float> min_sum = new ArrayList<>();
        min_sum.add(0F);
        min_sum.add(0F);

        dbDistances.open();
        dbSignatures.open();
        Cursor c = dbSignatures.getStudentSignature(student_id, lecture_id);
        Log.d(TAG, "Got " + c.getCount() + " rows. from student id " + student_id + " and lecture id = " + lecture_id);

        // Error in getting data from database.
        if(c.getCount() % 3 != 0) {
            //TODO: check if that can happen (e.g. phone is restarted), what to do?? maybe delete that user from database?
            Log.d(TAG, "Number of rows in database not mod 3. Some strange error.");
            return -4;
        }

        ArrayList<Float> x_normalised = DTW.normaliseXData(drawing_view.x_coord);
        ArrayList<Float> y_normalised = DTW.normaliseYData(drawing_view.y_coord);

        for (int i = 1; i <= c.getCount()/3; i++) {
            ArrayList<Float> x = getArray(c, "x", i);
            ArrayList<Float> y = getArray(c, "y", i);
            ArrayList<Float> p = getArray(c, "p", i);
            Log.d(TAG, "x koordinate iz baze" + x.toString());
            Log.d(TAG, "y koordinate iz baze" + y.toString());
            Log.d(TAG, "pen koordinate iz baze" + drawing_view.pen_start.toString());
            Log.d(TAG, "x koordinate trenutni potpis" + x_normalised.toString());
            Log.d(TAG, "y koordinate trenutni potpis" + y_normalised.toString());
            Log.d(TAG, "pen koordinate trenutni potpis" + p.toString());

            Log.d(TAG, "duljina iz baze je: " + x.size() + " " + y.size() +" " + p.size() );
            Log.d(TAG, "duljina live je: " + x_normalised.size() + " " + y_normalised.size() +" " + drawing_view.pen_start.size() );


            //Log.d(TAG, "min1 = " + DTW.calculateDistance1(x_coord_norm, y_coord_norm, pen_start, x, y, p));
            //Log.d(TAG, "min2 = " + DTW.calculateDistance2(x_coord_norm, y_coord_norm, pen_start, x, y, p));
            min1 = DTW.calculateDistance1(x_normalised, y_normalised, drawing_view.pen_start, x, y, p);
            min2 = DTW.calculateDistance2(x_normalised, y_normalised, drawing_view.pen_start, x, y, p);
            Log.d(TAG, "Minmal distance between signatures is: " + min1 +
                    " minimal distance2 is " + min2 + " min_sum before " + min_sum.toString());
            min_sum.set(0, min_sum.get(0) + min1);
            min_sum.set(1, min_sum.get(1) + min2);
            Log.d(TAG, " min_sum after " + min_sum.toString());
        }

        float min = Float.POSITIVE_INFINITY;
        ArrayList<Float> max_distances = dbDistances.getMaxDistance(student_id, lecture_id);
        for (int i = 0; i < max_distances.size(); i++) {
            min_sum.set(i, min_sum.get(i)/max_distances.get(i)/c.getCount()*3);
            if (min > min_sum.get(i))
                min = min_sum.get(i);
        }

        dbSignatures.close();
        dbDistances.close();
        Toast.makeText(getContext(), "Minmal distance between signatures is: " + min_sum.toString(), Toast.LENGTH_LONG).show();
        Log.d(TAG, "Minmal distance between signatures is: " + min_sum.toString());

        drawing_view.discardSignature();

        return min;
    }



    private ArrayList<Float> getArray(Cursor c, String axis, int signature_number)
    {
        ArrayList<Float> array = new ArrayList<>();
        int i=2;
        c.moveToFirst();
        do {
            //Log.d(TAG, "signature number = " + signature_number + " iz baze = " + c.getInt(0));
            //Log.d(TAG, "axis = " + axis + " iz baze = " + c.getString(1));
            if(c.getInt(0) == signature_number && c.getString(1).equals(axis))
                while (true) {
                    try {
                        float a = c.getFloat(i++);
                        if(a == 0)
                            return array;
                        array.add(a);
                    }
                    catch (Exception e) {
                        Log.d(TAG, "DOBIVENI ARRAY IZ BAZE " + array.toString());
                        return array;
                    }
                }
        } while (c.moveToNext());

        return array;
    }

}