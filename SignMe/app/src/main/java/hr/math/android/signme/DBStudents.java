package hr.math.android.signme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by ivan on 17.01.17..
 */

class DBStudents extends DBAdapter {

    private static final String TAG_SQL = "SQLStudents";

    DBStudents(Context ctx) {
        super(ctx);
    }

    int getStudentID(int lectureId, int jmbag)
    {
        int id = -1;
        Cursor mCursor = db.query(true, TABLE_STUDENTS, new String[] {ID},
                LECTURE_ID + "='" + lectureId + "' and " + JMBAG + "='" + jmbag + "'",
                null, null, null, null, null);
        if (mCursor != null && mCursor.moveToFirst()) {
            id = mCursor.getInt(0);
            mCursor.close();
        }

        Log.v(TAG_SQL, "Getting student id = " + id + " of student jmbag = " + jmbag
                + " and lecture id = " + lectureId);
        return id;
    }

    int getStudentID(int lectureId, int jmbag, String name, String surname)
    {
        int id = -1;
        Cursor mCursor = db.query(true, TABLE_STUDENTS, new String[] {ID, NAME, SURNAME},
                LECTURE_ID + "='" + lectureId + "' and " + JMBAG + "='" + jmbag
                        + "' and " + NAME + " LIKE '" + name + "' and " + SURNAME
                        + " LIKE '" + surname + "'", null, null, null, null, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            id = mCursor.getInt(0);
            mCursor.close();
        }
        Log.v(TAG_SQL, "Getting student id = " + id + " of student jmbag = " + jmbag
                + " and lecture id = " + lectureId);
        return id;
    }

    Cursor getAllStudentsOfLecture(int id)
    {
        Log.v(TAG_SQL, "Getting all students of lectureId " + id);
        return db.query(true, TABLE_STUDENTS, new String[] {ID, NAME, JMBAG},
                LECTURE_ID + "='" + id + "'", null, null, null, null, null);
    }

    Cursor getAllStudentsOfLecture(int id, CharSequence name)
    {
        Log.v(TAG_SQL, "Getting all students of lectureId = " + id + " with start (sur)name " + name);
        return db.query(true, TABLE_STUDENTS, new String[] {ID, NAME, SURNAME, JMBAG},
                LECTURE_ID + "='" + id + "' and (" + NAME + " LIKE '%" + name + "%' or "
                        + SURNAME + " LIKE '%" + name + "%')",
                null, null, null, null, null);
    }

    int numberOfStudents(int lectureId)
    {
        Log.v(TAG_SQL, "Number of students attending lecture " + lectureId);
        return getAllStudentsOfLecture(lectureId).getCount();
    }

    boolean deleteStudentFromLecture(int studentId, int lectureId) {
        Log.v(TAG_SQL, "Deleting student " + studentId + " from lecture " + lectureId);
        DBAttendance attendance = new DBAttendance(context);
        attendance.open();
        attendance.deleteStudentAttendance(studentId, lectureId);
        attendance.close();
        DBSignatures signatures = new DBSignatures(context);
        signatures.open();
        signatures.deleteSignature(studentId, lectureId);
        signatures.close();
        DBDistances distances = new DBDistances(context);
        distances.open();
        distances.deleteMaxDistance(studentId, lectureId);
        distances.close();
        return true;
    }

    boolean doesStudentExist(int lectureId, int jmbag)
    {
        Log.v(TAG_SQL, "Does student with jmbag " + jmbag + " exist on lecture " + lectureId);
        return getStudentID(lectureId, jmbag) != -1;
    }

    boolean doesStudentExist(int jmbag, int lectureId, String name, String surname)
    {
        Log.v(TAG_SQL, "Does student " + name + " " + surname + " with jmbag " + jmbag + " exist on lecture " + lectureId);
        return getStudentID(jmbag, lectureId, name, surname) != -1;
    }

    boolean newStudent(String name, String surname, int jmbag, int lectureId)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NAME, name);
        initialValues.put(SURNAME, surname);
        initialValues.put(JMBAG, jmbag);
        initialValues.put(LECTURE_ID, lectureId);
        Log.v(TAG_SQL, "Added student " + name + " to database + " + TABLE_STUDENTS);
        return db.insert(TABLE_STUDENTS, null, initialValues) > 0;
    }

}
