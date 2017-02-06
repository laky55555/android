package hr.math.android.signme.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by ivan on 17.01.17..
 */

public class DBStudents extends DBAdapter {

    private static final String TAG_SQL = "SQLStudents";

    public DBStudents(Context ctx) {
        super(ctx);
    }

    public int getStudentID(int lectureId, int jmbag)
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

    public int getStudentID(int lectureId, int jmbag, String name, String surname)
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

    public Cursor getAllStudentsOfLecture(int id)
    {
        Log.v(TAG_SQL, "Getting all students of lectureId " + id);
        return db.query(true, TABLE_STUDENTS, new String[] {ID, NAME, JMBAG},
                LECTURE_ID + "='" + id + "'", null, null, null, null, null);
    }

    public Pair<ArrayList<String>, ArrayList<Integer>> getAllStudentsOfLecture(int lectureId, boolean diff) {
        Log.v(TAG_SQL, "Getting all students of lectureId " + lectureId);
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setDistinct(true);
        builder.appendWhere(LECTURE_ID + " LIKE '" + lectureId + "'");
        builder.setTables(TABLE_STUDENTS);
        Cursor cursor = builder.query(db, new String[]{JMBAG, NAME, SURNAME, ID}, null, null, null, null, null, null);

        if(cursor != null && cursor.moveToFirst()) {
            ArrayList<String> students = new ArrayList<>();
            ArrayList<Integer> ids = new ArrayList<>();
            do {
                students.add(cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2));
                ids.add(cursor.getInt(3));
            } while (cursor.moveToNext());

            cursor.close();
            return Pair.create(students, ids);
        }

        return null;
    }


    public Cursor getAllStudentsOfLecture(int id, CharSequence name)
    {
        Log.v(TAG_SQL, "Getting all students of lectureId = " + id + " with start (sur)name " + name);
        return db.query(true, TABLE_STUDENTS, new String[] {ID, NAME, SURNAME, JMBAG},
                LECTURE_ID + "='" + id + "' and (" + NAME + " LIKE '%" + name + "%' or "
                        + SURNAME + " LIKE '%" + name + "%')",
                null, null, null, null, null);
    }

    public int numberOfStudents(int lectureId)
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

    public boolean doesStudentExist(int lectureId, int jmbag)
    {
        Log.v(TAG_SQL, "Does student with jmbag " + jmbag + " exist on lecture " + lectureId);
        return getStudentID(lectureId, jmbag) != -1;
    }

    boolean doesStudentExist(int jmbag, int lectureId, String name, String surname)
    {
        Log.v(TAG_SQL, "Does student " + name + " " + surname + " with jmbag " + jmbag + " exist on lecture " + lectureId);
        return getStudentID(jmbag, lectureId, name, surname) != -1;
    }

    public boolean newStudent(String name, String surname, int jmbag, int lectureId)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NAME, name);
        initialValues.put(SURNAME, surname);
        initialValues.put(JMBAG, jmbag);
        initialValues.put(LECTURE_ID, lectureId);
        Log.v(TAG_SQL, "Added student " + name + " to database + " + TABLE_STUDENTS);
        return db.insert(TABLE_STUDENTS, null, initialValues) > 0;
    }

    public String[] studentInfo(int studentId) {
        Log.v(TAG_SQL, "Getting info of student " + studentId);
        Cursor mCursor = db.query(true, TABLE_STUDENTS, new String[] {ID, NAME, SURNAME, JMBAG},
                ID + "='" + studentId + "'", null, null, null, null, null);
        if (mCursor != null && mCursor.moveToFirst()) {
            String name = mCursor.getString(1);
            String surname = mCursor.getString(2);
            String jmbag = mCursor.getString(3);
            mCursor.close();
            return new String[]{name, surname, jmbag};
        }
        return null;
    }

}
