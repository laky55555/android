package hr.math.android.signme;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

/**
 * Created by ivan on 17.01.17..
 */

public class DBLectures extends DBAdapter {

    static final String TAG_SQL = "SQLITE";

    static final String TABLE_LECTURES = "lectures";
    static final String LECTURE_ID = "_id";
    static final String LECTURE_NAME = "name";

    public DBLectures(Context ctx) {
        super(ctx);
    }


    public long newLecture(String name)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(LECTURE_NAME, name);
        Log.d(TAG_SQL, "Added lecture " + name + " to database.");
        return db.insert(TABLE_LECTURES, null, initialValues);
    }



}
