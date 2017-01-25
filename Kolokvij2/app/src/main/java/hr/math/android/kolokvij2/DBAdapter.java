package hr.math.android.kolokvij2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ilakovi on 1/25/17.
 */
public class DBAdapter {

    static final String ROWID = "_id";

    static final String NAME = "name";
    static final String EMAIL = "email";
    static final String SALARY = "salary";

    static final String MIN_SALARY = "minimal_salary";
    static final String RESPONSIBILITY = "responsibility";
    static final String POSITION_NAME = "position_name";


    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MyDB";
    static final String TABLE_EMPLOYEE = "employee";
    static final String TABLE_WORKPLACE = "workplace";
    static final int DATABASE_VERSION = 1;

    static final String CREATE_EMPLOYEE =
            "create table " + TABLE_EMPLOYEE + " (" + ROWID + " integer primary key autoincrement, "
                    + NAME + " text not null, " + EMAIL + " text not null, " + SALARY + " integer not null);";
    static final String CREATE_WORKPLACE =
            "create table " + TABLE_WORKPLACE + " (" + ROWID + " integer primary key autoincrement, "
                    + POSITION_NAME + " text not null, " + RESPONSIBILITY + " integer not null, " + MIN_SALARY + " integer not null);";

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
                db.execSQL(CREATE_EMPLOYEE);
                db.execSQL(CREATE_WORKPLACE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKPLACE);
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

    //---insert a contact into the database---
    public long insertEmployee(String name, String email, int salary)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NAME, name);
        initialValues.put(EMAIL, email);
        initialValues.put(SALARY, salary);
        return db.insert(TABLE_EMPLOYEE, null, initialValues);
    }


    public long insertWorkspace(String name, int responsibility, int minSalary)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(POSITION_NAME, name);
        initialValues.put(RESPONSIBILITY, responsibility);
        initialValues.put(MIN_SALARY, minSalary);
        return db.insert(TABLE_WORKPLACE, null, initialValues);
    }

//    //---deletes a particular contact---
//    public boolean deleteContact(long rowId)
//    {
//        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
//    }

    //---retrieves all the contacts---
    public Cursor getAllEmployees()
    {
        return db.query(TABLE_EMPLOYEE, new String[] {ROWID, NAME, EMAIL, SALARY},
                null, null, null, null, null);
    }

    public Cursor getAllWorkplaces()
    {
        return db.query(TABLE_WORKPLACE, new String[] {ROWID, POSITION_NAME, RESPONSIBILITY, MIN_SALARY},
                null, null, null, null, null);
    }


    public String getHighestPaying()
    {
        String names = "";
        Cursor c = db.query(TABLE_EMPLOYEE, new String[] {"MAX(" + SALARY + ")", NAME},
                        null, null, null, null, null);
        if(c.moveToFirst()) {
            int max = c.getInt(0);
            Cursor c2 = db.query(TABLE_EMPLOYEE, new String[] {NAME},
                    SALARY + "='" + max + "'", null, null, null, null);
            if(c2.moveToFirst()) {
                do {
                    names += c2.getString(0) + " ";
                } while (c2.moveToNext());
            }
        }

        return names;
    }

//    //---updates a contact---
//    public boolean updateContact(long rowId, String name, String email)
//    {
//        ContentValues args = new ContentValues();
//        args.put(KEY_NAME, name);
//        args.put(KEY_EMAIL, email);
//        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
//    }
//
//    public long insertAddress(String name, String street, Integer number)
//    {
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(KEY_NAME, name);
//        initialValues.put(KEY_STREET, street);
//        initialValues.put(KEY_NUMBER, number);
//        return db.insert(DATABASE_TABLE2, null, initialValues);
//    }
//
//    public Cursor getAllAddresses()
//    {
//        return db.query(DATABASE_TABLE2, new String[] {KEY_ROWID, KEY_NAME,
//                KEY_STREET, KEY_NUMBER}, null, null, null, null, null);
//    }

}