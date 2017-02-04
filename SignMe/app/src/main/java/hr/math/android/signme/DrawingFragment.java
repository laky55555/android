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
    private int numberOfSignatures = 1;
    private int numberOfDrawings = 3;
    private int studentId;
    private int lectureId;
    private boolean learningNewSignature;
    private DrawingView drawingView;
    private TextView text;

    private DBDistances dbDistances;
    private DBSignatures dbSignatures;


    public static DrawingFragment newInstance(boolean learningNew, int studentId, int lectureId) {
        DrawingFragment fragmentDemo = new DrawingFragment();
        Bundle args = new Bundle();
        args.putBoolean("learningNew", learningNew);
        args.putInt("studentId", studentId);
        args.putInt("lectureId", lectureId);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get back arguments
        learningNewSignature = getArguments().getBoolean("learningNew", false);
        studentId = getArguments().getInt("studentId", -1);
        lectureId = getArguments().getInt("lectureId", -1);
    }


    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_drawing, parent, false);
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

        if(learningNewSignature) {
            //TODO: za testirati, mislim da se prvi slucaj nece nikad dogoditi
            if(numberOfDrawings == 0)
                signedOnLesson(studentId, lectureId, 0);
            else
                text.setText(getResources().getString(R.string.please_sign)
                        + " " + Integer.toString(numberOfDrawings) + " " + getResources().getString(R.string.more_time));
        }
        else
            text.setText(R.string.please_sign);

        drawingView = (DrawingView) view.findViewById(R.id.drawing_view);

        saveButtonListener(view);

        view.findViewById(R.id.button_discard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Discard current signature", Toast.LENGTH_LONG).show();
                drawingView.discardSignature();
            }
        });
    }

    private void signedOnLesson(int studentId, int lectureId, float signatureDistance)
    {
        DBAttendance db = new DBAttendance(getContext());
        db.open();
        db.newAttendance(studentId, lectureId, signatureDistance);
        db.close();
        Toast.makeText(getContext(), "Starting fragment for finding student", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Starting fragment for finding student");
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment, FindStudentFragment.newInstance(lectureId));
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
                if(learningNewSignature) {
                    //int result = drawingView.saveSignature(numberOfSignatures, studentId, lectureId);
                    if (saveSignature() > 0)
                        updateNumberOfSignatures();
                }
                // If we are checking signature against already known signature in database
                else {
                    Toast.makeText(getActivity(), "Checking current signature", Toast.LENGTH_LONG).show();
                    float signatureResult = checkSignature();

                    if (signatureResult == -1)
                        signatureTooLong();
                    else if (signatureResult == -2)
                        signatureTooShort();
                    else
                        signedOnLesson(studentId, lectureId, signatureResult);
                }
            }
        });
    }

    private void updateNumberOfSignatures() {
        numberOfSignatures++;
        numberOfDrawings--;
        text.setText(getResources().getString(R.string.please_sign)
                + " " + Integer.toString(numberOfDrawings) + " " + getResources().getString(R.string.more_time));
        if (numberOfDrawings == 0)
            signedOnLesson(studentId, lectureId, 0);
    }

    private void signatureTooShort() {
        Log.d(TAG, "Too few coordinates. Number of coordinates must be over " + DBAdapter.MIN_NUM_OF_COORDS
                + " and we have " + drawingView.xCoordinates.size());
        Toast.makeText(getContext(), "Try to sign slower or choose longer signature ;).\nYou are too fast.", Toast.LENGTH_LONG).show();
    }

    private void signatureTooLong() {
        Log.d(TAG, "Too many coordinates. Number of coordinates must be less than " + DBAdapter.MAX_NUM_OF_COORDS
                + " and we have " + drawingView.xCoordinates.size());
        Toast.makeText(getContext(), "Try to sign faster ;).\nYou are too slow.", Toast.LENGTH_LONG).show();
    }

    private int checkCoordinatesInfo() {
        if (drawingView.xCoordinates.size() > DBAdapter.MAX_NUM_OF_COORDS ) {
            signatureTooLong();
            return -1;
        } else if(drawingView.xCoordinates.size() < DBAdapter.MIN_NUM_OF_COORDS ) {
            signatureTooShort();
            return -2;
        } else if(drawingView.xCoordinates.size() != drawingView.yCoordinates.size() ||
                drawingView.xCoordinates.size() != drawingView.penStart.size()) {
            Log.d(TAG, "Uneven number of coordinates for x, y and pen.");
            return -3;
        } else
            return 1;
    }

    //TODO: u save i load prvi dio ucitavanja podataka je jednak.
    private int saveSignature() {


        // Checking if we have too many, too few or error in number of coordinates
        int result = checkCoordinatesInfo();
        if (result < 0) {
            drawingView.discardSignature();
            return result;
        }

        Log.d(TAG, "x coord = " + drawingView.xCoordinates.toString());
        Log.d(TAG, "y coord = " + drawingView.yCoordinates.toString());
        Log.d(TAG, "pen start = " + drawingView.penStart.toString());
        Log.d(TAG, "Saving signature number " + numberOfSignatures + " of student "
                + studentId + " on lecture " + lectureId);
        Log.d(TAG, "duljina prije spremanja je: " + drawingView.xCoordinates.size() + " "
                + drawingView.yCoordinates.size() +" " + drawingView.penStart.size() );

        dbDistances.open();
        dbSignatures.open();

        Cursor c = dbSignatures.getStudentSignature(studentId, lectureId);
        Log.d(TAG, "Got " + c.getCount() + " rows.");

        // Error in getting data from database.
        if(c.getCount() % 3 != 0) {
            //TODO: check if that can happen (e.g. phone is restarted), what to do?? maybe delete that user from database?
            Log.d(TAG, "Number of rows in database not mod 3. Some strange error.");
            return -4;
        }

        ArrayList<Float> xNormalised = DTW.normaliseXData(drawingView.xCoordinates);
        ArrayList<Float> yNormalised = DTW.normaliseYData(drawingView.yCoordinates);

        dbSignatures.saveSignature(numberOfSignatures, studentId, lectureId, xNormalised, yNormalised, drawingView.penStart);

//        float max1 = 0, maxTemp1;
//        float max2 = 0, max_temp2;
        float maxTemp0, maxTemp1;
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
            Log.d(TAG, "x koordinate trenutni potpis" + xNormalised.toString());
            Log.d(TAG, "y koordinate trenutni potpis" + yNormalised.toString());
            Log.d(TAG, "pen koordinate trenutni potpis" + drawingView.penStart.toString());

            Log.d(TAG, "duljina iz baze je: " + x.size() + " " + y.size() +" " + p.size() );
            Log.d(TAG, "duljina live je: " + xNormalised.size() + " " + yNormalised.size() +" " + drawingView.penStart.size() );


            maxTemp0 = DTW.calculateDistance1(xNormalised, yNormalised, drawingView.penStart, x, y, p);
            maxTemp1 = DTW.calculateDistance2(xNormalised, yNormalised, drawingView.penStart, x, y, p);
            Log.d(TAG, "Maximal distance between signatures is: " + maxTemp0 +
                    "maximal distance2 is " + maxTemp1);
            if (maxTemp0 > max.get(0))
                max.set(0, maxTemp0);
            if (maxTemp1 > max.get(1))
                max.set(1, maxTemp1);
        }

        if(c.getCount() != 0)
            dbDistances.updateMaxDistance(studentId, lectureId, max);
        dbDistances.close();
        dbSignatures.close();
        drawingView.discardSignature();

        return 1;
    }


    private float checkSignature()
    {

        // Checking if we have too many, too few or error in number of coordinates
        int result = checkCoordinatesInfo();
        if (result < 0) {
            drawingView.discardSignature();
            return result;
        }

        float min1, min2;
        ArrayList<Float> minimalSum = new ArrayList<>();
        minimalSum.add(0F);
        minimalSum.add(0F);

        dbDistances.open();
        dbSignatures.open();
        Cursor c = dbSignatures.getStudentSignature(studentId, lectureId);
        Log.d(TAG, "Got " + c.getCount() + " rows. from student id " + studentId + " and lecture id = " + lectureId);

        // Error in getting data from database.
        if(c.getCount() % 3 != 0) {
            //TODO: check if that can happen (e.g. phone is restarted), what to do?? maybe delete that user from database?
            Log.d(TAG, "Number of rows in database not mod 3. Some strange error.");
            return -4;
        }

        ArrayList<Float> xNormalised = DTW.normaliseXData(drawingView.xCoordinates);
        ArrayList<Float> yNormalised = DTW.normaliseYData(drawingView.yCoordinates);

        for (int i = 1; i <= c.getCount()/3; i++) {
            ArrayList<Float> x = getArray(c, "x", i);
            ArrayList<Float> y = getArray(c, "y", i);
            ArrayList<Float> p = getArray(c, "p", i);
            Log.d(TAG, "x koordinate iz baze" + x.toString());
            Log.d(TAG, "y koordinate iz baze" + y.toString());
            Log.d(TAG, "pen koordinate iz baze" + drawingView.penStart.toString());
            Log.d(TAG, "x koordinate trenutni potpis" + xNormalised.toString());
            Log.d(TAG, "y koordinate trenutni potpis" + yNormalised.toString());
            Log.d(TAG, "pen koordinate trenutni potpis" + p.toString());

            Log.d(TAG, "duljina iz baze je: " + x.size() + " " + y.size() +" " + p.size() );
            Log.d(TAG, "duljina live je: " + xNormalised.size() + " " + yNormalised.size() +" " + drawingView.penStart.size() );


            //Log.d(TAG, "min1 = " + DTW.calculateDistance1(xNormalised, yNormalised, drawingView.penStart, x, y, p));
            //Log.d(TAG, "min2 = " + DTW.calculateDistance2(xNormalised, yNormalised, drawingView.penStart, x, y, p));
            min1 = DTW.calculateDistance1(xNormalised, yNormalised, drawingView.penStart, x, y, p);
            min2 = DTW.calculateDistance2(xNormalised, yNormalised, drawingView.penStart, x, y, p);
            Log.d(TAG, "Minmal distance between signatures is: " + min1 +
                    " minimal distance2 is " + min2 + " minimalSum before " + minimalSum.toString());
            minimalSum.set(0, minimalSum.get(0) + min1);
            minimalSum.set(1, minimalSum.get(1) + min2);
            Log.d(TAG, " minimalSum after " + minimalSum.toString());
        }

        float min = Float.POSITIVE_INFINITY;
        ArrayList<Float> maxDistances = dbDistances.getMaxDistance(studentId, lectureId);
        for (int i = 0; i < maxDistances.size(); i++) {
            minimalSum.set(i, minimalSum.get(i)/maxDistances.get(i)/c.getCount()*3);
            if (min > minimalSum.get(i))
                min = minimalSum.get(i);
        }

        dbSignatures.close();
        dbDistances.close();
        Toast.makeText(getContext(), "Minmal distance between signatures is: " + minimalSum.toString(), Toast.LENGTH_LONG).show();
        Log.d(TAG, "Minmal distance between signatures is: " + minimalSum.toString());

        drawingView.discardSignature();

        return min;
    }



    private ArrayList<Float> getArray(Cursor c, String axis, int signatureNumber)
    {
        ArrayList<Float> array = new ArrayList<>();
        int i=2;
        c.moveToFirst();
        do {
            //Log.d(TAG, "signature number = " + signatureNumber + " iz baze = " + c.getInt(0));
            //Log.d(TAG, "axis = " + axis + " iz baze = " + c.getString(1));
            if(c.getInt(0) == signatureNumber && c.getString(1).equals(axis))
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