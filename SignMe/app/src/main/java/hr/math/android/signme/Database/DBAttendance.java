package hr.math.android.signme.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import hr.math.android.signme.Database.DBAdapter;

/**
 * Created by ivan on 17.01.17..
 */

public class DBAttendance extends DBAdapter {

    private static final String TAG_SQL = "SQLAttendance";

    public DBAttendance(Context ctx) {
        super(ctx);
    }

    public boolean newAttendance(int studentId, int lectureId, float signatureDistance)
    {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String now = df.format(new Date());

        String findQuery = STUDENT_ID + "='" + studentId + "' and "
                + LECTURE_ID + "='" + lectureId + "' and "
                + DATE + " LIKE '" + now + "'";

        Cursor c = db.query(true, TABLE_ATTENDANCES, new String[] {ID, DISTANCE},
                findQuery, null, null, null, null, null);
        if(c.getCount() == 1) {
            c.moveToFirst();
            Log.v(TAG_SQL, "Somebody already signed student = " + studentId + " today.");
            float min_signatureDistance =
                    signatureDistance < c.getFloat(1) ? signatureDistance : c.getFloat(1);
            ContentValues args = new ContentValues();
            args.put(REMARK, "Multiple_signatures");
            args.put(DISTANCE, min_signatureDistance);
            return db.update(TABLE_ATTENDANCES, args, findQuery, null) > 0;
        }
        else {
            Log.v(TAG_SQL, "Add student = " + studentId + " into attendance of lecture " + lectureId);
            ContentValues initialValues = new ContentValues();
            initialValues.put(STUDENT_ID, studentId);
            initialValues.put(LECTURE_ID, lectureId);
            initialValues.put(DATE, now);
            initialValues.put(DISTANCE, signatureDistance);
            return db.insert(TABLE_ATTENDANCES, null, initialValues) > 0;
        }
    }

    boolean deleteStudentAttendance(int studentId, int lectureId)
    {
        Log.v(TAG_SQL, "Deleting studentId = " + studentId + ", lectureId = "
                + lectureId + " from table " + TABLE_ATTENDANCES);
        return db.delete(TABLE_ATTENDANCES, STUDENT_ID + "='" + studentId + "'" + " and "
                + LECTURE_ID + "='" + lectureId + "'", null) > 0;
    }


    public ArrayList<String> getAllDatesOfLectureOfStudent(int studentId, int lectureId) {
        Log.v(TAG_SQL, "Getting all dates of lecture " + lectureId + " student " + studentId);
        Cursor cursor = db.query(true, TABLE_ATTENDANCES, new String[] {ID, DATE},
                LECTURE_ID + "='" + lectureId + "' AND " + STUDENT_ID + "='" + studentId + "'", null, null, null, null, null);
        Log.v(TAG_SQL, "cursor = " + cursor);
        if(cursor != null && cursor.moveToFirst()) {
            Log.v(TAG_SQL, "Cursor size " + cursor.getCount());
            ArrayList<String> dates = new ArrayList<>();
            do {
                dates.add(cursor.getString(1));
            } while (cursor.moveToNext());

            cursor.close();
            return dates;
        }

        return null;
    }


    public ArrayList<String> getAllStudentsOfLectureOfDate(String date, int lectureId) {
        Log.v(TAG_SQL, "Getting all students of lectureId " + lectureId + " date" + date);
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setDistinct(true);
        builder.appendWhere(TABLE_ATTENDANCES + "." + LECTURE_ID + "='" + lectureId + "' AND ");
        builder.appendWhere(DATE + " LIKE '" + date + "'");

        builder.setTables(TABLE_ATTENDANCES + " INNER JOIN " + TABLE_STUDENTS + " ON("
                + TABLE_ATTENDANCES + "." + LECTURE_ID + "=" + TABLE_STUDENTS + "." + LECTURE_ID + " AND "
                + TABLE_ATTENDANCES + "." + STUDENT_ID + "=" + TABLE_STUDENTS + "." + ID + ")");
        Cursor cursor = builder.query(db, new String[]{JMBAG, NAME, SURNAME, STUDENT_ID, DATE}, null, null, null, null, null, null);

        if(cursor != null && cursor.moveToFirst()) {
            ArrayList<String> students = new ArrayList<>();
            do {
                students.add(cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2));
            } while (cursor.moveToNext());

            cursor.close();
            return students;
        }

        return null;
    }


    public ArrayList<String> getAllDatesOfLecture(int lectureId) {

        Log.v(TAG_SQL, "Getting all dates of lecture " + lectureId);
        Cursor cursor = db.query(true, TABLE_ATTENDANCES, new String[] {DATE},
                LECTURE_ID + "='" + lectureId + "'", null, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            ArrayList<String> dates = new ArrayList<>();
            do {
                dates.add(cursor.getString(0));
            } while (cursor.moveToNext());

            cursor.close();
            return dates;
        }

        return null;
    }

    Cursor getLecture(int lectureId) {
        Log.v(TAG_SQL, "Getting all info of lectureId " + lectureId);
        return db.query(true, TABLE_ATTENDANCES, new String[] {ID, DATE, DISTANCE, LECTURE_ID, STUDENT_ID , REMARK},
                    LECTURE_ID + "='" + lectureId + "'", null, null, null, null, null);
    }

    //select * from attendances left outer join students on(attendances.id_lecture=students.id_lecture and attendances.id_student=students._id);
    //select id_lecture, name from attendances, students where attendances.id_lecture=students.id_lecture and attendances.id_student=students._id;

    public Cursor getAllStudentsOfLecture(int lectureId) {
        Log.v(TAG_SQL, "Getting all students of lectureId " + lectureId);
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setDistinct(true);
        builder.setTables(TABLE_ATTENDANCES + " INNER JOIN " + TABLE_STUDENTS + " ON("
                + TABLE_ATTENDANCES + "." + LECTURE_ID + "=" + TABLE_STUDENTS + "." + LECTURE_ID + " AND "
                + TABLE_ATTENDANCES + "." + STUDENT_ID + "=" + TABLE_STUDENTS + "." + ID + ")");
        return builder.query(db, new String[]{JMBAG, NAME, SURNAME, STUDENT_ID}, TABLE_ATTENDANCES + "." + LECTURE_ID + "='" + lectureId + "'",
                null, null, null, null, null);
    }



    public String hasAttended(int studentId, int lectureId, String date) {
        Log.v(TAG_SQL, "Checking if student " + studentId + " attended lecture " + lectureId + " on date " + date);
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setDistinct(true);
        builder.setTables(TABLE_ATTENDANCES);
        builder.appendWhere(LECTURE_ID + "='" + lectureId + "' AND ");
        builder.appendWhere(STUDENT_ID + "='" + studentId + "' AND ");
        builder.appendWhere(DATE + " LIKE '" + date + "'");
        Cursor c = builder.query(db, new String[]{REMARK}, null, null, null, null, null, null);
        if(c == null) {
            return "?";
        } else if(c.getCount() == 0) {
            return "-";
        } else {
            if(!c.moveToFirst())
                return "?";
            Log.d(TAG_SQL, "DObiveno iz tablice je '" + c.getString(0) + "'");
            if(c.getString(0) == null)
                return "+";
            else
                return c.getString(0);
        }

    }
}
