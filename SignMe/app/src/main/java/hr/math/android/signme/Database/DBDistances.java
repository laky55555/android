package hr.math.android.signme.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import hr.math.android.signme.Database.DBAdapter;

/**
 * Created by ivan on 17.01.17..
 */

public class DBDistances extends DBAdapter {

    private static final String TAG_SQL = "SQLDistances";

    public DBDistances(Context ctx) {
        super(ctx);
    }

    public boolean updateMaxDistance(int studentId, int lectureId, ArrayList<Float> max)
    {
        String findQuery = STUDENT_ID + "='" + studentId + "' and "
                + LECTURE_ID+ "='" + lectureId + "'";

        ContentValues args = new ContentValues();
        String[] columns = new String[max.size() + 1];

        args.put(LECTURE_ID, lectureId);
        args.put(STUDENT_ID, studentId);

        for (int i = 0; i < max.size(); i++) {
            args.put(DISTANCE + i, max.get(i));
            columns[i] = DISTANCE + i;
        }

        Cursor c = db.query(true, TABLE_MAX_DISTANCES, columns, findQuery, null, null, null, null, null);
        // If maximum already exist in table update result.
        if(c.getCount() == 1) {
            c.moveToFirst();

            // Update all columns with maximal element
            for (int i = 0; i < max.size(); i++)
                if(c.getFloat(i) > max.get(i))
                    args.put(DISTANCE + i, c.getFloat(i));

            c.close();

            for (int i = 0; i < max.size(); i++)
                Log.v(TAG_SQL, "Updated in table on: " + args.get(DISTANCE + i));

            return db.update(TABLE_MAX_DISTANCES, args, findQuery, null) == 1;
        }
        else {
            Log.v(TAG_SQL, "Inserted in table: " + max.toString());
            c.close();
            return db.insert(TABLE_MAX_DISTANCES, null, args) > 0;
        }
    }

    public ArrayList<Float> getMaxDistance(int studentId, int lectureId)
    {
        ArrayList<Float> array = new ArrayList<>();
        String findQuery = STUDENT_ID + "='" + studentId + "' and "
                + LECTURE_ID+ "='" + lectureId + "'";
        //TODO: tu je isto hardcodiran broj algoritama s kojim usporedujemo. Vidi todo na DBAdapter.
        Cursor c = db.query(true, TABLE_MAX_DISTANCES, new String[] {ID, DISTANCE+"0", DISTANCE+"1"},
                findQuery, null, null, null, null, null);
        if(c != null && c.moveToFirst()) {
            array.add(c.getFloat(1));
            array.add(c.getFloat(2));
            c.close();
        }
        return array;
    }

    boolean deleteMaxDistance(int studentId, int lectureId)
    {
        Log.v(TAG_SQL, "Deleting studentId = " + studentId + ", lectureId = "
                + lectureId + " from table " + TABLE_MAX_DISTANCES);
        return db.delete(TABLE_MAX_DISTANCES, STUDENT_ID + "='" + studentId + "'" + " and "
                + LECTURE_ID + "='" + lectureId + "'", null) > 0;
    }


}
