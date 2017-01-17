package hr.math.android.signme;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


class DBAdapter {

    static final String TAG_SQL = "SQLITE";

    static final String TABLE_LECTURES = "lectures";
    static final String LECTURE_ID = "_id";
    static final String LECTURE_NAME = "name";

    private static final String CREATE_LECTURES =
            "create table " + TABLE_LECTURES + " (" + LECTURE_ID + " integer primary key autoincrement, "
                    + LECTURE_NAME + " text not null);";

    static final String TABLE_STUDENTS = "students";
    static final String STUDENT_ID = "_id";
    static final String STUDENT_NAME = "name";
    static final String STUDENT_SURNAME = "surname";
    static final String STUDENT_JMBAG = "JMBAG";
    static final String STUDENT_LECTURE_ID = "id_lecture";

    private static final String CREATE_STUDENTS =
            "create table " + TABLE_STUDENTS + " (" + STUDENT_ID + " integer primary key autoincrement, "
                    + STUDENT_NAME + " text not null, " + STUDENT_SURNAME + " text not null, "
                    + STUDENT_JMBAG + " integer not null, " + STUDENT_LECTURE_ID + " integer, "
                    + "FOREIGN KEY(" + STUDENT_LECTURE_ID + ") REFERENCES " + TABLE_LECTURES + "(" + LECTURE_ID + "));";


    static final String TABLE_ATTENDANCES = "attendance";
    static final String ATTENDANCE_ID = "_id";
    static final String ATTENDANCE_DATE = "date";
    static final String ATTENDANCE_DISTANCE = "signature_distance";
    static final String ATTENDANCE_LECTURE_ID = "lecture_id";
    static final String ATTENDANCE_STUDENT_ID = "student_id";
    static final String ATTENDANCE_REMARK = "remark";

    private static final String CREATE_ATTENDANCES =
            "create table " + TABLE_ATTENDANCES + " (" + ATTENDANCE_ID + " integer primary key autoincrement, "
                    + ATTENDANCE_DATE + " text not null, " + ATTENDANCE_DISTANCE + " real not null, "
                    + ATTENDANCE_LECTURE_ID + " integer, " + ATTENDANCE_STUDENT_ID + " integer, " + ATTENDANCE_REMARK + " text, "
                    + "FOREIGN KEY(" + ATTENDANCE_LECTURE_ID + ") REFERENCES " + TABLE_LECTURES + "(" + LECTURE_ID + "), "
                    + "FOREIGN KEY(" + ATTENDANCE_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + STUDENT_ID + "));";

    //TODO: odluciti da li ce se studenti moci potpisivati drugacije na drugim predmetima?? u pocetku DA!;
    //      kad e to bude mijenalo treba paziti na nacin brisanja studenata jer treba vidjeti koliko predmeta student slusa
    //      te ako ga brisemo iz zadnjeg kojeg slusa tada brisemo potpis, do sad brisemo potpis kad brisemo predmet
    //TODO: treba se odlučiti koliko koordinata za potpise ce svaki student imati za sad 1000
    static final int number_of_coords = 1000;
    static final String TABLE_SIGNATURES = "signatures";
    static final String SIGNATURE_ID = "_id";
    static final String SIGNATURE_STUDENT_ID = "student_id";
    static final String SIGNATURE_LECTURE_ID = "lecture_id";
    static final String SIGNATURE_AXIS = "axis";
    static final String SIGNATURE_COORD = "coord";
    static final String SIGNATURE_NUMBER = "signature_number";

    private static String CREATE_SIGNATURES;
//            "create table " + TABLE_SIGNATURES + " (" + SIGNATURE_ID + " integer primary key autoincrement, "
//                    + SIGNATURE_STUDENT_ID + " integer, " + SIGNATURE_AXIS + " text not null";
//    for(int i = 0; i < number_of_coords; i++)
//        CREATE_SIGNATURES += ", " + SIGNATURE_COORD + Integer.toString(i) + " real";
//    CREATE_SIGNATURES += ", FOREIGN KEY(" + SIGNATURE_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + STUDENT_ID + "));";

    static private String createSignaturesTable() {
         String table =
                 "create table " + TABLE_SIGNATURES + " (" + SIGNATURE_ID + " integer primary key autoincrement, "
                + SIGNATURE_STUDENT_ID + " integer, " + SIGNATURE_LECTURE_ID + " integer, " + SIGNATURE_AXIS
                + " text not null, " + SIGNATURE_NUMBER + " integer not null";
        for(int i = 0; i < number_of_coords; i++)
            table += ", " + SIGNATURE_COORD + Integer.toString(i) + " real";
        table += ", FOREIGN KEY(" + SIGNATURE_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + STUDENT_ID + ")";
        table += ", FOREIGN KEY(" + SIGNATURE_LECTURE_ID + ") REFERENCES " + TABLE_LECTURES + "(" + LECTURE_ID + "));";
        return table;
    }

    static final String TABLE_MAX_DISTANCES = "max_distances";
    static final String  MAX_DISTANCE_ID = "_id";
    static final String  MAX_DISTANCE_DISTANCE = "distance";
    static final String  MAX_DISTANCE_STUDENT_ID = "student_id";
    static final String  MAX_DISTANCE_LECTURE_ID = "lecture_id";

    private static final String CREATE_MAX_DISTANCES =
            "create table " + TABLE_MAX_DISTANCES + " (" + MAX_DISTANCE_ID + " integer primary key autoincrement, "
                    + MAX_DISTANCE_DISTANCE + "0 real not null, " + MAX_DISTANCE_DISTANCE + "1 real, " + MAX_DISTANCE_DISTANCE + "2 real, "
                    + MAX_DISTANCE_LECTURE_ID + " integer, " + MAX_DISTANCE_STUDENT_ID + " integer, "
                    + "FOREIGN KEY(" + MAX_DISTANCE_LECTURE_ID + ") REFERENCES " + TABLE_LECTURES + "(" + LECTURE_ID + "), "
                    + "FOREIGN KEY(" + MAX_DISTANCE_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + STUDENT_ID + "));";


    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "MyDB";
    private static final int DATABASE_VERSION = 2;


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
                db.execSQL(CREATE_STUDENTS);
                CREATE_SIGNATURES = createSignaturesTable();
                db.execSQL(CREATE_SIGNATURES);
                db.execSQL(CREATE_ATTENDANCES);
                db.execSQL(CREATE_MAX_DISTANCES);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGNATURES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LECTURES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAX_DISTANCES);
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

//    public long newLecture(String name)
//    {
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(LECTURE_NAME, name);
//        Log.d(TAG_SQL, "Added lecture " + name + " to database.");
//        return db.insert(TABLE_LECTURES, null, initialValues);
//    }

//    public boolean deleteAllLectures()
//    {
//        Log.d(TAG_SQL, "Dropping all tables.");
//        db.execSQL("DELETE FROM " + TABLE_ATTENDANCES);
//        db.execSQL("DELETE FROM " + TABLE_SIGNATURES);
//        db.execSQL("DELETE FROM " + TABLE_STUDENTS);
//        db.execSQL("DELETE FROM " + TABLE_LECTURES);
//        db.execSQL("DELETE FROM " + TABLE_MAX_DISTANCES);
//
//        db.execSQL("VACUUM");
//        return true;
//    }

//    public int getLectureID(String name)
//    {
//        int lecture_id = -1;
//        Cursor mCursor =
//                db.query(true, TABLE_LECTURES, new String[] {LECTURE_ID},
//                        LECTURE_NAME + "='" + name + "'", null, null, null, null, null);
//
//        if (mCursor != null && mCursor.moveToFirst()) {
//            lecture_id = mCursor.getInt(0);
//        }
//
//        Log.d(TAG_SQL, "Getting lecture id, lecutre = " + name + " ID = " + lecture_id);
//        return lecture_id;
//    }

//    public boolean deleteLecture(String name)
//    {
//        int ID = getLectureID(name);
//        if(ID == -1) {
//            Log.d(TAG_SQL, "No lecture with name " + name);
//            return false;
//        }
//        return deleteLectureByID(ID);
//    }
//
//    public boolean deleteLectureByID(int lecture_id)
//    {
//        // Get all students that listen given lecture so we can delete all their signatures and attendance.
//        Cursor student_cursor = getAllStudentsOfLecture(lecture_id);
//        if (student_cursor.moveToFirst()) {
//            do {
//                deleteStudentFromLecture(student_cursor.getInt(0), lecture_id);
//            } while (student_cursor.moveToNext());
//        }
//
//        Log.d(TAG_SQL, "Deleting lecture_id = " + lecture_id + " from table " + TABLE_LECTURES);
//        return db.delete(TABLE_LECTURES, LECTURE_ID + "='" + lecture_id + "'", null) > 0;
//    }

//    public Cursor getAllLectures()
//    {
//        Cursor c = db.query(TABLE_LECTURES, new String[] {LECTURE_ID, LECTURE_NAME},
//                null, null, null, null, null);
//        //c.setNotificationUri();
//        return c;
//    }
//
//    /**
//     * Check if lecture with given name already exist.
//     * @param name Name of lecture we want to check if already exits.
//     * @return true if lecture already exist, else false.
//     */
//    public boolean doesLectureExist(String name) {
//        Cursor c = getAllLectures();
//        if (c.moveToFirst()) {
//            do {
//                if(name.equals(c.getString(1)))
//                    return true;
//            } while (c.moveToNext());
//        }
//
//        return false;
//    }


//    public int getStudentID(int lecture_id, int jmbag)
//    {
//        int id = -1;
//        Cursor mCursor = db.query(true, TABLE_STUDENTS, new String[] {STUDENT_ID},
//                STUDENT_LECTURE_ID + "='" + lecture_id + "' and " + STUDENT_JMBAG + "='" + jmbag + "'",
//                null, null, null, null, null);
//        if (mCursor != null && mCursor.moveToFirst()) {
//            id = mCursor.getInt(0);
//        }
//
//        Log.d(TAG_SQL, "Getting student id = " + id + " of student jmbag = " + jmbag
//                + " and lecture id = " + lecture_id);
//        return id;
//    }
//
//    public int getStudentID(int lecture_id, int jmbag, String name, String surname)
//    {
//        int id = -1;
//        Cursor mCursor = db.query(true, TABLE_STUDENTS, new String[] {STUDENT_ID, STUDENT_NAME, STUDENT_SURNAME},
//                STUDENT_LECTURE_ID + "='" + lecture_id + "' and " + STUDENT_JMBAG + "='" + jmbag
//                        + "' and " + STUDENT_NAME + " LIKE '" + name + "' and " + STUDENT_SURNAME
//                        + " LIKE '" + surname + "'", null, null, null, null, null);
//
//        if (mCursor != null && mCursor.moveToFirst()) {
//            id = mCursor.getInt(0);
//        }
//
//        Log.d(TAG_SQL, "Getting student id = " + id + " of student jmbag = " + jmbag
//                + " and lecture id = " + lecture_id);
//        return id;
//    }
//
//    public Cursor getAllStudentsOfLecture(int id)
//    {
//        Log.d(TAG_SQL, "Getting all students of lecture_id = " + id);
//        return db.query(true, TABLE_STUDENTS, new String[] {STUDENT_ID, STUDENT_NAME, STUDENT_JMBAG},
//                        STUDENT_LECTURE_ID + "='" + id + "'", null, null, null, null, null);
//    }
//
//    public Cursor getAllStudentsOfLecture(int id, CharSequence name)
//    {
//        Log.d(TAG_SQL, "Getting all students of lecture_id = " + id);
//        return db.query(true, TABLE_STUDENTS, new String[] {STUDENT_ID, STUDENT_NAME, STUDENT_SURNAME, STUDENT_JMBAG},
//                        STUDENT_LECTURE_ID + "='" + id + "' and (" + STUDENT_NAME + " LIKE '%" + name + "%' or "
//                                + STUDENT_SURNAME + " LIKE '%" + name + "%')",
//                        null, null, null, null, null);
//    }
//
//    public int numberOfStudents(int lecture_id)
//    {
//        return db.query(true, TABLE_STUDENTS, new String[] {STUDENT_ID, STUDENT_NAME, STUDENT_JMBAG},
//                STUDENT_LECTURE_ID + "='" + lecture_id + "'" , null, null, null, null, null).getCount();
//    }
//
//    public boolean deleteStudentFromLecture(int student_id, int lecture_id) {
//        deleteStudentAttendance(student_id, lecture_id);
//        deleteSignature(student_id, lecture_id);
//        deleteMaxDistance(student_id, lecture_id);
//        return true;
//    }
//
//    private boolean deleteStudentAttendance(int student_id, int lecture_id)
//    {
//        Log.d(TAG_SQL, "Deleting student_id = " + student_id + ", lecture_id = "
//                + lecture_id + " from table " + TABLE_ATTENDANCES);
//        return db.delete(TABLE_ATTENDANCES, ATTENDANCE_STUDENT_ID + "='" + student_id + "'" + " and "
//                + ATTENDANCE_LECTURE_ID + "='" + lecture_id + "'", null) > 0;
//    }
//    private boolean deleteSignature(int student_id, int lecture_id)
//    {
//        Log.d(TAG_SQL, "Deleting student_id = " + student_id + ", lecture_id = "
//                + lecture_id + " from table " + TABLE_SIGNATURES);
//        return db.delete(TABLE_SIGNATURES, SIGNATURE_STUDENT_ID + "='" + student_id + "'" + " and "
//                + SIGNATURE_LECTURE_ID + "='" + lecture_id + "'", null) > 0;
//    }
//    private boolean deleteMaxDistance(int student_id, int lecture_id)
//    {
//        Log.d(TAG_SQL, "Deleting student_id = " + student_id + ", lecture_id = "
//                + lecture_id + " from table " + TABLE_MAX_DISTANCES);
//        return db.delete(TABLE_MAX_DISTANCES, SIGNATURE_STUDENT_ID + "='" + student_id + "'" + " and "
//                + SIGNATURE_LECTURE_ID + "='" + lecture_id + "'", null) > 0;
//    }
//
//    public boolean doesStudentExist(int jmbag, int lecture_id)
//    {
//        return getStudentID(jmbag, lecture_id) != -1;
//        /*Cursor c = db.query(true, TABLE_STUDENTS, new String[] {STUDENT_ID, STUDENT_NAME, STUDENT_SURNAME},
//                STUDENT_LECTURE_ID + "='" + lecture_id + "' and " + STUDENT_JMBAG + "='" + jmbag + "'" ,
//                null, null, null, null, null);
//        if (c.moveToFirst()) {
//            Log.d(TAG_SQL, "Student with jmbag " + jmbag + " already exit, student is: " + c.getString(1)
//                    + " " + c.getString(2));
//            return true;
//        }
//
//        return false;*/
//    }
//
//    public boolean doesStudentExist(int jmbag, int lecture_id, String name, String surname)
//    {
//        return getStudentID(jmbag, lecture_id, name, surname) != -1;
//        /*Cursor c = db.query(true, TABLE_STUDENTS, new String[] {STUDENT_ID, STUDENT_NAME, STUDENT_SURNAME},
//                STUDENT_LECTURE_ID + "='" + lecture_id + "' and " + STUDENT_JMBAG + "='" + jmbag
//                + "' and " + STUDENT_NAME + " LIKE '" + name + "' and " + STUDENT_SURNAME
//                + " LIKE '" + surname + "'", null, null, null, null, null);
//        if (c.moveToFirst()) {
//            Log.d(TAG_SQL, "Student with jmbag " + jmbag + " already exit, student is: " + c.getString(1)
//                    + " " + c.getString(2));
//            return true;
//        }
//
//        return false;*/
//    }
//
//    public boolean newStudent(String name, String surname, int JMBAG, int lecture_id)
//    {
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(STUDENT_NAME, name);
//        initialValues.put(STUDENT_SURNAME, surname);
//        initialValues.put(STUDENT_JMBAG, JMBAG);
//        initialValues.put(STUDENT_LECTURE_ID, lecture_id);
//        Log.d(TAG_SQL, "Added student " + name + " to database + " + TABLE_STUDENTS);
//        return db.insert(TABLE_STUDENTS, null, initialValues) > 0;
//    }

//    public Cursor getStudentSignature(int student_id, int lecture_id)
//    {
//        String find_query = SIGNATURE_STUDENT_ID + "='" + student_id + "' and "
//                + SIGNATURE_LECTURE_ID+ "='" + lecture_id + "'";
//        String[] getRows = new String[number_of_coords+2];
//        getRows[0] = SIGNATURE_NUMBER;
//        getRows[1] = SIGNATURE_AXIS;
//        for (int i = 0; i < number_of_coords; i++)
//            getRows[i+2] = SIGNATURE_COORD + i;
//
//        return db.query(true, TABLE_SIGNATURES, getRows, find_query, null, null, null, null, null);
//    }
//
//
//    public boolean saveSignature(int number, int student_id, int lecture_id, ArrayList<Float> x_coord,
//                                 ArrayList<Float> y_coord, ArrayList<Float> pen_up)
//    {
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(SIGNATURE_STUDENT_ID, student_id);
//        initialValues.put(SIGNATURE_LECTURE_ID, lecture_id);
//        initialValues.put(SIGNATURE_NUMBER, number);
//
//        initialValues.put(SIGNATURE_AXIS, "x");
//        for (int i = 0; i < x_coord.size(); i++)
//            initialValues.put(SIGNATURE_COORD+i, x_coord.get(i));
//        db.insert(TABLE_SIGNATURES, null, initialValues);
//
//        initialValues.put(SIGNATURE_AXIS, "y");
//        for (int i = 0; i < x_coord.size(); i++)
//            initialValues.put(SIGNATURE_COORD+i, y_coord.get(i));
//        db.insert(TABLE_SIGNATURES, null, initialValues);
//
//        initialValues.put(SIGNATURE_AXIS, "p");
//        for (int i = 0; i < x_coord.size(); i++)
//            initialValues.put(SIGNATURE_COORD+i, pen_up.get(i));
//        db.insert(TABLE_SIGNATURES, null, initialValues);
//        return true;
//    }

//    public boolean newAttendance(int student_id, int lecture_id, float signature_distance)
//    {
//        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
//        String now = df.format(new Date());
//
//        String find_query = ATTENDANCE_STUDENT_ID + "='" + student_id + "' and "
//                + ATTENDANCE_LECTURE_ID + "='" + lecture_id + "' and "
//                + ATTENDANCE_DATE + " LIKE '" + now + "'";
//
//        Cursor c = db.query(true, TABLE_ATTENDANCES, new String[] {ATTENDANCE_ID, ATTENDANCE_DISTANCE},
//                find_query, null, null, null, null, null);
//        if(c.getCount() == 1) {
//            c.moveToFirst();
//            Log.d(TAG_SQL, "Somebody already signed student = " + student_id + " today.");
//            float min_signature_distance =
//                    signature_distance < c.getFloat(1) ? signature_distance : c.getFloat(1);
//            ContentValues args = new ContentValues();
//            args.put(ATTENDANCE_REMARK, "Multiple_signatures");
//            args.put(ATTENDANCE_DISTANCE, min_signature_distance);
//            return db.update(TABLE_ATTENDANCES, args, find_query, null) > 0;
//        }
//        else {
//            Log.d(TAG_SQL, "Add student = " + student_id + " into attendance of lecture " + lecture_id);
//            ContentValues initialValues = new ContentValues();
//            initialValues.put(ATTENDANCE_STUDENT_ID, student_id);
//            initialValues.put(ATTENDANCE_LECTURE_ID, lecture_id);
//            initialValues.put(ATTENDANCE_DATE, now);
//            initialValues.put(ATTENDANCE_DISTANCE, signature_distance);
//            return db.insert(TABLE_ATTENDANCES, null, initialValues) > 0;
//        }
//    }



//    public boolean updateMaxDistance(int student_id, int lecture_id, float max1, float max2)
//    {
//        String find_query = MAX_DISTANCE_STUDENT_ID + "='" + student_id + "' and "
//                + MAX_DISTANCE_LECTURE_ID+ "='" + lecture_id + "'";
//
//        ContentValues args = new ContentValues();
//        args.put(MAX_DISTANCE_DISTANCE + "0", max1);
//        args.put(MAX_DISTANCE_DISTANCE + "1", max2);
//
//        Cursor c = db.query(true, TABLE_MAX_DISTANCES, new String[] {MAX_DISTANCE_ID, MAX_DISTANCE_DISTANCE+"0", MAX_DISTANCE_DISTANCE+"1"},
//                find_query, null, null, null, null, null);
//        if(c.getCount() == 1) {
//            c.moveToFirst();
//            if(c.getFloat(1) > max1)
//                args.put(MAX_DISTANCE_DISTANCE + "0", c.getFloat(1));
//            if(c.getFloat(2) > max2)
//                args.put(MAX_DISTANCE_DISTANCE + "1", c.getFloat(2));
//
//            Log.d(TAG_SQL, "Updated in table on: " + args.get(MAX_DISTANCE_DISTANCE + "0") + " and " + args.get(MAX_DISTANCE_DISTANCE+"1") );
//            return db.update(TABLE_MAX_DISTANCES, args, find_query, null) == 1;
//        }
//        else {
//            args.put(MAX_DISTANCE_DISTANCE + "0", max1);
//            args.put(MAX_DISTANCE_DISTANCE + "1", max2);
//            args.put(MAX_DISTANCE_LECTURE_ID, lecture_id);
//            args.put(MAX_DISTANCE_STUDENT_ID, student_id);
//            Log.d(TAG_SQL, "Inserted in table: " + max1 + " and " + max2);
//            return db.insert(TABLE_MAX_DISTANCES, null, args) > 0;
//        }
//    }
//
//    public ArrayList<Float> getMaxDistance(int student_id, int lecture_id)
//    {
//        ArrayList<Float> array = new ArrayList<>();
//        String find_query = MAX_DISTANCE_STUDENT_ID + "='" + student_id + "' and "
//                + MAX_DISTANCE_LECTURE_ID+ "='" + lecture_id + "'";
//        Cursor c = db.query(true, TABLE_MAX_DISTANCES, new String[] {MAX_DISTANCE_ID, MAX_DISTANCE_DISTANCE+"0", MAX_DISTANCE_DISTANCE+"1"},
//                find_query, null, null, null, null, null);
//        c.moveToFirst();
//        array.add(c.getFloat(1));
//        array.add(c.getFloat(2));
//        return array;
//    }

    /*public boolean saveSignature(int number, int student_id, int lecture_id,
                                 ArrayList array, String axis)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(SIGNATURE_STUDENT_ID, student_id);
        initialValues.put(SIGNATURE_LECTURE_ID, lecture_id);
        initialValues.put(SIGNATURE_NUMBER, number);

        initialValues.put(SIGNATURE_AXIS, axis);
        for (int i = 0; i < array.size(); i++)
            initialValues.put(SIGNATURE_COORD+i, (Float) array.get(i));
        return db.insert(TABLE_SIGNATURES, null, initialValues) > 0;
    }*/

//    int newLesson(String table_name) {
//        int today_lesson = -1;
//        Cursor mCursor =
//                db.query(true, table_name, new String[] {}, LECTURES_ROWID + "=" + 1, null,
//                        null, null, null, null);
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//            today_lesson = mCursor.getColumnCount();
//            Log.d(TAG_SQL, "number of columns = " + Integer.toString(mCursor.getColumnCount()));
//            //for(int i = 0; i < mCursor.getColumnCount(); i++)
//                //Log.d(TAG_SQL, "columns = " + mCursor.getString(i));
//            for(int i = 1; i < mCursor.getColumnCount(); i++) {
//                //Log.d(TAG_SQL, "columns = " + mCursor.getString(i));
//                if(mCursor.getString(i).equals("None")) {
//                    today_lesson = i-1;
//                    break;
//                }
//            }
//        }
//        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
//        String now = df.format(new Date());
//        Log.d(TAG_SQL, "date = " + now);
//        Log.d(TAG_SQL, "Prvi stupac za promjenu je lesson" + today_lesson);
//        ContentValues args = new ContentValues();
//        args.put("lesson"+Integer.toString(today_lesson), now);
//        db.update(table_name, args, LECTURES_ROWID + "= '" + 1 + "'", null);
//        return today_lesson;
//    }


}