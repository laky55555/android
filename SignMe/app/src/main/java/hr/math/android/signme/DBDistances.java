package hr.math.android.signme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ivan on 17.01.17..
 */

class DBDistances extends DBAdapter {

    private static final String TAG_SQL = "SQLDistances";

    DBDistances(Context ctx) {
        super(ctx);
    }

    boolean updateMaxDistance(int student_id, int lecture_id, float max1, float max2)
    {
        String findQuery = MAX_DISTANCE_STUDENT_ID + "='" + student_id + "' and "
                + MAX_DISTANCE_LECTURE_ID+ "='" + lecture_id + "'";

        ContentValues args = new ContentValues();
        args.put(MAX_DISTANCE_DISTANCE + "0", max1);
        args.put(MAX_DISTANCE_DISTANCE + "1", max2);

        Cursor c = db.query(true, TABLE_MAX_DISTANCES, new String[] {MAX_DISTANCE_ID, MAX_DISTANCE_DISTANCE+"0", MAX_DISTANCE_DISTANCE+"1"},
                findQuery, null, null, null, null, null);
        if(c.getCount() == 1) {
            c.moveToFirst();
            if(c.getFloat(1) > max1)
                args.put(MAX_DISTANCE_DISTANCE + "0", c.getFloat(1));
            if(c.getFloat(2) > max2)
                args.put(MAX_DISTANCE_DISTANCE + "1", c.getFloat(2));
            c.close();
            Log.d(TAG_SQL, "Updated in table on: " + args.get(MAX_DISTANCE_DISTANCE + "0") + " and " + args.get(MAX_DISTANCE_DISTANCE+"1") );
            return db.update(TABLE_MAX_DISTANCES, args, findQuery, null) == 1;
        }
        else {
            args.put(MAX_DISTANCE_DISTANCE + "0", max1);
            args.put(MAX_DISTANCE_DISTANCE + "1", max2);
            args.put(MAX_DISTANCE_LECTURE_ID, lecture_id);
            args.put(MAX_DISTANCE_STUDENT_ID, student_id);
            Log.d(TAG_SQL, "Inserted in table: " + max1 + " and " + max2);
            c.close();
            return db.insert(TABLE_MAX_DISTANCES, null, args) > 0;
        }
    }

    ArrayList<Float> getMaxDistance(int student_id, int lecture_id)
    {
        ArrayList<Float> array = new ArrayList<>();
        String findQuery = MAX_DISTANCE_STUDENT_ID + "='" + student_id + "' and "
                + MAX_DISTANCE_LECTURE_ID+ "='" + lecture_id + "'";
        Cursor c = db.query(true, TABLE_MAX_DISTANCES, new String[] {MAX_DISTANCE_ID, MAX_DISTANCE_DISTANCE+"0", MAX_DISTANCE_DISTANCE+"1"},
                findQuery, null, null, null, null, null);
        c.moveToFirst();
        array.add(c.getFloat(1));
        array.add(c.getFloat(2));
        c.close();
        return array;
    }

}
