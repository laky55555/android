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

    boolean updateMaxDistance(int studentId, int lectureId, ArrayList<Float> max)
    {
        String findQuery = MAX_DISTANCE_STUDENT_ID + "='" + studentId + "' and "
                + MAX_DISTANCE_LECTURE_ID+ "='" + lectureId + "'";

        ContentValues args = new ContentValues();
        String[] columns = new String[max.size() + 1];

        args.put(MAX_DISTANCE_LECTURE_ID, lectureId);
        args.put(MAX_DISTANCE_STUDENT_ID, studentId);

        for (int i = 0; i < max.size(); i++) {
            args.put(MAX_DISTANCE_DISTANCE + i, max.get(i));
            columns[i] = MAX_DISTANCE_DISTANCE + i;
        }

        Cursor c = db.query(true, TABLE_MAX_DISTANCES, columns, findQuery, null, null, null, null, null);
        if(c.getCount() == 1) {
            c.moveToFirst();

            // Update all columns with maximal element
            for (int i = 0; i < max.size(); i++)
                if(c.getFloat(i) > max.get(i))
                    args.put(MAX_DISTANCE_DISTANCE + i, c.getFloat(i));

            c.close();

            for (int i = 0; i < max.size(); i++)
                Log.d(TAG_SQL, "Updated in table on: " + args.get(MAX_DISTANCE_DISTANCE + i));

            return db.update(TABLE_MAX_DISTANCES, args, findQuery, null) == 1;
        }
        else {
            Log.d(TAG_SQL, "Inserted in table: " + max.toString());
            c.close();
            return db.insert(TABLE_MAX_DISTANCES, null, args) > 0;
        }
    }

    ArrayList<Float> getMaxDistance(int studentId, int lectureId)
    {
        ArrayList<Float> array = new ArrayList<>();
        String findQuery = MAX_DISTANCE_STUDENT_ID + "='" + studentId + "' and "
                + MAX_DISTANCE_LECTURE_ID+ "='" + lectureId + "'";
        Cursor c = db.query(true, TABLE_MAX_DISTANCES, new String[] {MAX_DISTANCE_ID, MAX_DISTANCE_DISTANCE+"0", MAX_DISTANCE_DISTANCE+"1"},
                findQuery, null, null, null, null, null);
        c.moveToFirst();
        array.add(c.getFloat(1));
        array.add(c.getFloat(2));
        c.close();
        return array;
    }

}
