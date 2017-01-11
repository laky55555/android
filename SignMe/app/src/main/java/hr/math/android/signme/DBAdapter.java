package hr.math.android.signme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBAdapter {

    static final String TABLE_LECTURES = "lectures";
    static final String LECTURES_ROWID = "_id";
    static final String LECTURES_NAME = "lecture";
    static final String LECTURES_ID = "lecture_id";
    static final String LESSONS_NUMBER = "number_of_lessons";

    private static final String CREATE_LECTURES =
            "create table " + TABLE_LECTURES + " (" + LECTURES_ROWID + " integer primary key autoincrement, "
                    + LECTURES_NAME + " text not null, " + LECTURES_ID + " integer, " + LESSONS_NUMBER + " integer);";


//    static final String KEY_EMAIL = "email";
//
//    static final String KEY_STREET = "street";
//    static final String KEY_NUMBER = "number";
//

    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "MyDB";
    private static final int DATABASE_VERSION = 1;


//    static final String DATABASE_CREATE2 =
//            "create table addresses (_id integer primary key autoincrement, "
//                    + "name text not null, street text not null, number integer not null);";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(CREATE_LECTURES);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    public long newLecture(String name, Long ID, int number_of_lessons)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(LECTURES_NAME, name);
        initialValues.put(LECTURES_ID, ID);
        initialValues.put(LESSONS_NUMBER, number_of_lessons);
        Log.d("SQLITE", "Added " + name + " with id " + ID.toString() + " and "
                + number_of_lessons + " lessons to database.");
        return db.insert(TABLE_LECTURES, null, initialValues);
    }

    public boolean deleteAllLectures()
    {
        Cursor c = getAllLectures();
        while (c.moveToNext()) {
            String dropQuery = "DROP TABLE IF EXISTS " + c.getString(0);
            Log.d("SQLITE", "Dropping table " + c.getString(0));
            db.execSQL(dropQuery);
        }

        return db.delete(TABLE_LECTURES, null, null) > 0;
    }

    public boolean deleteLecture(String name)
    {
        db.delete(TABLE_LECTURES, LECTURES_NAME + "=" + "'" + name + "'", null);
        return db.delete(name, null, null) > 0;
    }

    public Cursor getAllLectures()
    {
        return db.query(TABLE_LECTURES, new String[] {LECTURES_ROWID, LECTURES_NAME,
                LECTURES_ID}, null, null, null, null, null);
    }

    public Cursor getLectureByID(long ID) throws SQLException
    {
        Cursor mCursor =
                db.query(true, TABLE_LECTURES, new String[] {LECTURES_ROWID,
                                LECTURES_NAME, LECTURES_ID}, LECTURES_ID + "=" + ID, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean doesExist(String name) {
        Cursor c = getAllLectures();
        if (c.moveToFirst()) {
            do {
                if(name.equals(c.getString(1)))
                    return true;
            } while (c.moveToNext());
        }

        return false;
    }

    public boolean updateLectureByID(long ID, String name)
    {
        ContentValues args = new ContentValues();
        args.put(LECTURES_NAME, name);
        return db.update(TABLE_LECTURES, args, LECTURES_ID + "=" + ID, null) > 0;
    }

    // Bez modifikator pristupa jer je za samo za paket
    boolean createNewTable(String name, int number_of_lessons) {
        //TU fali zarez na kraju autoincrement
        ContentValues initialValues = new ContentValues();
        String CREATE_TABLE = "create table " + name + " (" + LECTURES_ROWID + " integer primary key autoincrement";
        for(int i=0; i<number_of_lessons; i++) {
            CREATE_TABLE += ", lesson" + Integer.toString(i) + " text";
            initialValues.put("lesson" + Integer.toString(i), "None");
        }
        CREATE_TABLE += ");";

        try {
            db.execSQL(CREATE_TABLE);
            db.insert(name, null, initialValues);
        } catch (SQLException e) {
            Log.d("SQLITE", e.toString());
            return false;
        }
        return true;
    }

    int newLesson(String table_name) {
        int today_lesson = -1;
        Cursor mCursor =
                db.query(true, table_name, new String[] {}, LECTURES_ROWID + "=" + 1, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            today_lesson = mCursor.getColumnCount();
            Log.d("SQLITE", "number of columns = " + Integer.toString(mCursor.getColumnCount()));
            //for(int i = 0; i < mCursor.getColumnCount(); i++)
                //Log.d("SQLITE", "columns = " + mCursor.getString(i));
            for(int i = 1; i < mCursor.getColumnCount(); i++) {
                //Log.d("SQLITE", "columns = " + mCursor.getString(i));
                if(mCursor.getString(i).equals("None")) {
                    today_lesson = i-1;
                    break;
                }
            }
        }
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String now = df.format(new Date());
        Log.d("SQLITE", "date = " + now);
        Log.d("SQLITE", "Prvi stupac za promjenu je lesson" + today_lesson);
        ContentValues args = new ContentValues();
        args.put("lesson"+Integer.toString(today_lesson), now);
        db.update(table_name, args, LECTURES_ROWID + "= '" + 1 + "'", null);
        return today_lesson;
    }


}