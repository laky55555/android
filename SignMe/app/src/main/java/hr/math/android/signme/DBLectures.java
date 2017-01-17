package hr.math.android.signme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by ivan on 17.01.17..
 */

class DBLectures extends DBAdapter {

    private static final String TAG_SQL = "SQLLectures";

    DBLectures(Context ctx) {
        super(ctx);
    }


    long newLecture(String name)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(LECTURE_NAME, name);
        Log.d(TAG_SQL, "Added lecture " + name + " to database.");
        return db.insert(TABLE_LECTURES, null, initialValues);
    }

    boolean deleteAllLectures()
    {
        Log.d(TAG_SQL, "Dropping all tables.");
        db.execSQL("DELETE FROM " + TABLE_ATTENDANCES);
        db.execSQL("DELETE FROM " + TABLE_SIGNATURES);
        db.execSQL("DELETE FROM " + TABLE_STUDENTS);
        db.execSQL("DELETE FROM " + TABLE_LECTURES);
        db.execSQL("DELETE FROM " + TABLE_MAX_DISTANCES);
        // TODO: PISE DA JE DOBRO NAPRAVITI VACUUM???
        db.execSQL("VACUUM");
        return true;
    }

    int getLectureID(String name)
    {
        int lectureId = -1;
        Cursor mCursor =
                db.query(true, TABLE_LECTURES, new String[] {LECTURE_ID},
                        LECTURE_NAME + "='" + name + "'", null, null, null, null, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            lectureId = mCursor.getInt(0);
            mCursor.close();

        }
        Log.d(TAG_SQL, "Getting lecture id, lecutre = " + name + " ID = " + lectureId);
        return lectureId;
    }

    boolean deleteLecture(String name)
    {
        int ID = getLectureID(name);
        if(ID == -1) {
            Log.d(TAG_SQL, "No lecture with name " + name);
            return false;
        }
        return deleteLectureByID(ID);
    }

    private boolean deleteLectureByID(int lectureId)
    {
        // Get all students that listen given lecture so we can delete all their signatures and attendance.
        DBStudents students = new DBStudents(context);
        students.open();
        Cursor student_cursor = students.getAllStudentsOfLecture(lectureId);
        if (student_cursor.moveToFirst()) {
            do {
                students.deleteStudentFromLecture(student_cursor.getInt(0), lectureId);
            } while (student_cursor.moveToNext());
        }
        students.close();
        Log.d(TAG_SQL, "Deleting lectureId = " + lectureId + " from table " + TABLE_LECTURES);
        return db.delete(TABLE_LECTURES, LECTURE_ID + "='" + lectureId + "'", null) > 0;
    }

    Cursor getAllLectures()
    {
        return db.query(TABLE_LECTURES, new String[] {LECTURE_ID, LECTURE_NAME},
                null, null, null, null, null);
    }

    /**
     * Check if lecture with given name already exist.
     * @param name Name of lecture we want to check if already exits.
     * @return true if lecture already exist, else false.
     */
    boolean doesLectureExist(String name) {
        Cursor c = getAllLectures();
        if (c.moveToFirst()) {
            do {
                if(name.equals(c.getString(1)))
                    return true;
            } while (c.moveToNext());
        }

        return false;
    }


}
