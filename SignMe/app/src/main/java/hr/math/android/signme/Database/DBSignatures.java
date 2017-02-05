package hr.math.android.signme.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import hr.math.android.signme.Database.DBAdapter;

/**
 * Created by ivan on 17.01.17..
 */

public class DBSignatures extends DBAdapter {

    private static final String TAG_SQL = "SQLSignature";

    public DBSignatures(Context ctx) {
        super(ctx);
    }

    public Cursor getStudentSignature(int studentId, int lectureId)
    {
        String findQuery = STUDENT_ID + "='" + studentId + "' and "
                + LECTURE_ID+ "='" + lectureId + "'";
        String[] getRows = new String[MAX_NUM_OF_COORDS +2];
        getRows[0] = SIGNATURE_NUMBER;
        getRows[1] = AXIS;
        for (int i = 0; i < MAX_NUM_OF_COORDS; i++)
            getRows[i+2] = COORDINATE + i;

        Log.v(TAG_SQL, "Getting signature from student " + studentId + " on lecture " + lectureId);
        return db.query(true, TABLE_SIGNATURES, getRows, findQuery, null, null, null, null, null);
    }


    public boolean saveSignature(int number, int studentId, int lectureId, ArrayList<Float> xCoord,
            ArrayList<Float> yCoord, ArrayList<Float> penUp)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(STUDENT_ID, studentId);
        initialValues.put(LECTURE_ID, lectureId);
        initialValues.put(SIGNATURE_NUMBER, number);

        initialValues.put(AXIS, "x");
        for (int i = 0; i < xCoord.size(); i++)
            initialValues.put(COORDINATE+i, xCoord.get(i));
        db.insert(TABLE_SIGNATURES, null, initialValues);

        initialValues.put(AXIS, "y");
        for (int i = 0; i < xCoord.size(); i++)
            initialValues.put(COORDINATE+i, yCoord.get(i));
        db.insert(TABLE_SIGNATURES, null, initialValues);

        initialValues.put(AXIS, "p");
        for (int i = 0; i < xCoord.size(); i++)
            initialValues.put(COORDINATE+i, penUp.get(i));
        db.insert(TABLE_SIGNATURES, null, initialValues);
        Log.v(TAG_SQL, "Inserting new signature for student " + studentId + " on lecture " + lectureId);
        return true;
    }

    boolean deleteSignature(int studentId, int lectureId)
    {
        Log.v(TAG_SQL, "Deleting studentId = " + studentId + ", lectureId = "
                + lectureId + " from table " + TABLE_SIGNATURES);
        return db.delete(TABLE_SIGNATURES, STUDENT_ID + "='" + studentId + "'" + " and "
                + LECTURE_ID + "='" + lectureId + "'", null) > 0;
    }

}
