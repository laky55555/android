package hr.math.android.signme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by ivan on 17.01.17..
 */

class DBSignatures extends DBAdapter{

    private static final String TAG_SQL = "SQLSignature";

    DBSignatures(Context ctx) {
        super(ctx);
    }

    Cursor getStudentSignature(int studentId, int lectureId)
    {
        String findQuery = SIGNATURE_STUDENT_ID + "='" + studentId + "' and "
                + SIGNATURE_LECTURE_ID+ "='" + lectureId + "'";
        String[] getRows = new String[DBAdapter.MAX_NUM_OF_COORDS+2];
        getRows[0] = SIGNATURE_NUMBER;
        getRows[1] = SIGNATURE_AXIS;
        for (int i = 0; i < DBAdapter.MAX_NUM_OF_COORDS; i++)
            getRows[i+2] = SIGNATURE_COORD + i;

        return db.query(true, TABLE_SIGNATURES, getRows, findQuery, null, null, null, null, null);
    }


    boolean saveSignature(int number, int student_id, int lecture_id, ArrayList<Float> xCoord,
                                 ArrayList<Float> yCoord, ArrayList<Float> penUp)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(SIGNATURE_STUDENT_ID, student_id);
        initialValues.put(SIGNATURE_LECTURE_ID, lecture_id);
        initialValues.put(SIGNATURE_NUMBER, number);

        initialValues.put(SIGNATURE_AXIS, "x");
        for (int i = 0; i < xCoord.size(); i++)
            initialValues.put(SIGNATURE_COORD+i, xCoord.get(i));
        db.insert(TABLE_SIGNATURES, null, initialValues);

        initialValues.put(SIGNATURE_AXIS, "y");
        for (int i = 0; i < xCoord.size(); i++)
            initialValues.put(SIGNATURE_COORD+i, yCoord.get(i));
        db.insert(TABLE_SIGNATURES, null, initialValues);

        initialValues.put(SIGNATURE_AXIS, "p");
        for (int i = 0; i < xCoord.size(); i++)
            initialValues.put(SIGNATURE_COORD+i, penUp.get(i));
        db.insert(TABLE_SIGNATURES, null, initialValues);
        return true;
    }

}
