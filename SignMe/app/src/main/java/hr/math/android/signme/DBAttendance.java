package hr.math.android.signme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by ivan on 17.01.17..
 */

class DBAttendance extends DBAdapter {

    private static final String TAG_SQL = "SQLAttendance";

    DBAttendance(Context ctx) {
        super(ctx);
    }

    boolean newAttendance(int studentId, int lectureId, float signatureDistance)
    {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String now = df.format(new Date());

        String findQuery = ATTENDANCE_STUDENT_ID + "='" + studentId + "' and "
                + ATTENDANCE_LECTURE_ID + "='" + lectureId + "' and "
                + ATTENDANCE_DATE + " LIKE '" + now + "'";

        Cursor c = db.query(true, TABLE_ATTENDANCES, new String[] {ATTENDANCE_ID, ATTENDANCE_DISTANCE},
                findQuery, null, null, null, null, null);
        if(c.getCount() == 1) {
            c.moveToFirst();
            Log.d(TAG_SQL, "Somebody already signed student = " + studentId + " today.");
            float min_signatureDistance =
                    signatureDistance < c.getFloat(1) ? signatureDistance : c.getFloat(1);
            ContentValues args = new ContentValues();
            args.put(ATTENDANCE_REMARK, "Multiple_signatures");
            args.put(ATTENDANCE_DISTANCE, min_signatureDistance);
            return db.update(TABLE_ATTENDANCES, args, findQuery, null) > 0;
        }
        else {
            Log.d(TAG_SQL, "Add student = " + studentId + " into attendance of lecture " + lectureId);
            ContentValues initialValues = new ContentValues();
            initialValues.put(ATTENDANCE_STUDENT_ID, studentId);
            initialValues.put(ATTENDANCE_LECTURE_ID, lectureId);
            initialValues.put(ATTENDANCE_DATE, now);
            initialValues.put(ATTENDANCE_DISTANCE, signatureDistance);
            return db.insert(TABLE_ATTENDANCES, null, initialValues) > 0;
        }
    }
}
