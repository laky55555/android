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
        Cursor mCursor = db.query(true, TABLE_STUDENTS, new String[] {STUDENT_ID},
                STUDENT_LECTURE_ID + "='" + lectureId + "' and " + STUDENT_JMBAG + "='" + jmbag + "'",
                null, null, null, null, null);
        if (mCursor != null && mCursor.moveToFirst()) {
            id = mCursor.getInt(0);
        }

        Log.d(TAG_SQL, "Getting student id = " + id + " of student jmbag = " + jmbag
                + " and lecture id = " + lectureId);
        return id;
    }

    int getStudentID(int lectureId, int jmbag, String name, String surname)
    {
        int id = -1;
        Cursor mCursor = db.query(true, TABLE_STUDENTS, new String[] {STUDENT_ID, STUDENT_NAME, STUDENT_SURNAME},
                STUDENT_LECTURE_ID + "='" + lectureId + "' and " + STUDENT_JMBAG + "='" + jmbag
                        + "' and " + STUDENT_NAME + " LIKE '" + name + "' and " + STUDENT_SURNAME
                        + " LIKE '" + surname + "'", null, null, null, null, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            id = mCursor.getInt(0);
        }

        Log.d(TAG_SQL, "Getting student id = " + id + " of student jmbag = " + jmbag
                + " and lecture id = " + lectureId);
        return id;
    }

    Cursor getAllStudentsOfLecture(int id)
    {
        Log.d(TAG_SQL, "Getting all students of lectureId = " + id);
        return db.query(true, TABLE_STUDENTS, new String[] {STUDENT_ID, STUDENT_NAME, STUDENT_JMBAG},
                STUDENT_LECTURE_ID + "='" + id + "'", null, null, null, null, null);
    }

    Cursor getAllStudentsOfLecture(int id, CharSequence name)
    {
        Log.d(TAG_SQL, "Getting all students of lectureId = " + id);
        return db.query(true, TABLE_STUDENTS, new String[] {STUDENT_ID, STUDENT_NAME, STUDENT_SURNAME, STUDENT_JMBAG},
                STUDENT_LECTURE_ID + "='" + id + "' and (" + STUDENT_NAME + " LIKE '%" + name + "%' or "
                        + STUDENT_SURNAME + " LIKE '%" + name + "%')",
                null, null, null, null, null);
    }

    int numberOfStudents(int lectureId)
    {
        return db.query(true, TABLE_STUDENTS, new String[] {STUDENT_ID, STUDENT_NAME, STUDENT_JMBAG},
                STUDENT_LECTURE_ID + "='" + lectureId + "'" , null, null, null, null, null).getCount();
    }

    boolean deleteStudentFromLecture(int studentId, int lectureId) {
        deleteStudentAttendance(studentId, lectureId);
        deleteSignature(studentId, lectureId);
        deleteMaxDistance(studentId, lectureId);
        return true;
    }

    private boolean deleteStudentAttendance(int studentId, int lectureId)
    {
        Log.d(TAG_SQL, "Deleting studentId = " + studentId + ", lectureId = "
                + lectureId + " from table " + TABLE_ATTENDANCES);
        return db.delete(TABLE_ATTENDANCES, ATTENDANCE_STUDENT_ID + "='" + studentId + "'" + " and "
                + ATTENDANCE_LECTURE_ID + "='" + lectureId + "'", null) > 0;
    }
    private boolean deleteSignature(int studentId, int lectureId)
    {
        Log.d(TAG_SQL, "Deleting studentId = " + studentId + ", lectureId = "
                + lectureId + " from table " + TABLE_SIGNATURES);
        return db.delete(TABLE_SIGNATURES, SIGNATURE_STUDENT_ID + "='" + studentId + "'" + " and "
                + SIGNATURE_LECTURE_ID + "='" + lectureId + "'", null) > 0;
    }
    private boolean deleteMaxDistance(int studentId, int lectureId)
    {
        Log.d(TAG_SQL, "Deleting studentId = " + studentId + ", lectureId = "
                + lectureId + " from table " + TABLE_MAX_DISTANCES);
        return db.delete(TABLE_MAX_DISTANCES, SIGNATURE_STUDENT_ID + "='" + studentId + "'" + " and "
                + SIGNATURE_LECTURE_ID + "='" + lectureId + "'", null) > 0;
    }

    boolean doesStudentExist(int lectureId, int jmbag)
    {
        return getStudentID(lectureId, jmbag) != -1;
    }

    boolean doesStudentExist(int jmbag, int lectureId, String name, String surname)
    {
        return getStudentID(jmbag, lectureId, name, surname) != -1;
    }

    boolean newStudent(String name, String surname, int JMBAG, int lectureId)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(STUDENT_NAME, name);
        initialValues.put(STUDENT_SURNAME, surname);
        initialValues.put(STUDENT_JMBAG, JMBAG);
        initialValues.put(STUDENT_LECTURE_ID, lectureId);
        Log.d(TAG_SQL, "Added student " + name + " to database + " + TABLE_STUDENTS);
        return db.insert(TABLE_STUDENTS, null, initialValues) > 0;
    }

}
