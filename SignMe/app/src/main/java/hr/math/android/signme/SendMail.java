package hr.math.android.signme;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import hr.math.android.signme.Database.DBAttendance;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by mira on 14.01.17..
 */

public class SendMail {

    private static String TAG = "Send Mail";
    // Row from which we start putting students. Must have one for header and one for title.
    private static int startRow = 2;
    // Column from which header starts. Must have 3 free for JMBAG, name, surname.
    private static int startColumn = 3;


    private static void fillHeader(WritableSheet sheet, String lectureName, Context context) {
        try {
            sheet.addCell(new Label(2, startRow - 2, lectureName)); // column and row
            sheet.addCell(new Label(0, startRow - 1, context.getString(R.string.student_id))); // column and row
            sheet.addCell(new Label(1, startRow - 1, context.getString(R.string.name))); // column and row
            sheet.addCell(new Label(2, startRow - 1, context.getString(R.string.surname))); // column and row
        } catch (Exception e) {
            Log.d(TAG, "Error in filling header: " + e);
        }
    }

    private static int[] fillStudents(WritableSheet sheet, Cursor students) {
        int studentIds[] = new int[students.getCount()];
        try {
            int row = startRow;
            if (students.moveToFirst()) {
                do {
                    Log.v(TAG, students.getString(0) + " " + students.getString(1) + " " + students.getString(2) + ", row " + row);
                    sheet.addCell(new Label(0, row, students.getString(0))); // column and row
                    sheet.addCell(new Label(1, row, students.getString(1))); // column and row
                    sheet.addCell(new Label(2, row, students.getString(2))); // column and row
                    studentIds[row - startRow] = students.getInt(3);
                    row++;
                } while (students.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error in filling students: " + e);
        }

        return studentIds;
    }

    private static String[] fillDates(WritableSheet sheet, Cursor dates) {
        String dateList[] = new String[dates.getCount()];
        try {
            int column = startColumn;
            if (dates.moveToFirst()) {
                do {
                    Log.v(TAG, dates.getString(0) + " column " + column);
                    sheet.addCell(new Label(column, startRow-1, dates.getString(0))); // column and row
                    dateList[column - startColumn] = dates.getString(0);
                    column++;
                } while (dates.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error in filling dates: " + e);
        }

        return dateList;
    }
    private static void fillAttendance(WritableSheet sheet, DBAttendance attendance, int[] studentsIds, String[] dateList, int lectureId) {
        try {
            for (int i=0; i<studentsIds.length; i++) {
                for (int j=0; j<dateList.length; j++) {
                    String answer = attendance.hasAttended(studentsIds[i], lectureId, dateList[j]);
                    Log.v(TAG, "student " + studentsIds[i] + " on date" + dateList[j] + " answered " + answer);
                    sheet.addCell(new Label(startColumn + j, startRow + i, answer));
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Error in filling attendance: " + e);
        }
    }

    private static boolean fillTable(WritableSheet sheet, Context context, int lectureId, String lectureName) {
        DBAttendance attendance = new DBAttendance(context);
        attendance.open();
        Cursor students = attendance.getAllStudentsOfLecture(lectureId);
        Cursor dates = attendance.getAllDatesOfLecture(lectureId);
        if(students == null || dates == null)
            return false;
        Log.v(TAG, "Number of students = " + students.getCount());
        Log.v(TAG, "Number of dates = " + dates.getCount());

        fillHeader(sheet, lectureName, context);
        int studentIds[] = fillStudents(sheet, students);
        String dateList[] = fillDates(sheet, dates);
        fillAttendance(sheet, attendance, studentIds, dateList, lectureId);

        attendance.close();
        return true;
    }

    private static void exportToExcel(Context context, int[] lectureIds, String[] lectureNames, File[] files) {

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;
        try {
            for (int i=0; i<lectureIds.length; i++) {
                workbook = Workbook.createWorkbook(files[i], wbSettings);
                //Excel sheet name. 0 represents first sheet
                WritableSheet sheet = workbook.createSheet(lectureNames[i], i);
                fillTable(sheet, context, lectureIds[i], lectureNames[i]);
                workbook.write();
                try {
                    workbook.close();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File[] createFile(String[] lectureNames) {
        File root = android.os.Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        File dir = new File(root.getAbsolutePath());
        dir.mkdirs();
        //if(dir.mkdirs())
        File[] files = new File[lectureNames.length];
        for (int i=0; i<lectureNames.length; i++)
            files[i] = new File(dir, lectureNames[i]+".xls");

        return files;
    }

    public static Intent sendMail(Context context, int[] lectureIds, String[] lectureNames){

        Preferences preferences = new Preferences(context);
        if(!preferences.isEmailInitialized())
            return null;
        String email = preferences.getEmail();

        File[] files = createFile(lectureNames);
        Log.v(TAG, "File path = " + files[0].getAbsolutePath());
        exportToExcel(context, lectureIds, lectureNames, files);

        // Date for email subject.
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String now = df.format(new Date());

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
        //Intent emailIntent = new Intent(Intent.ACTION_SEND);
        //emailIntent.setType("text/plain");
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.mail_subject) + now);
        emailIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.mail_text) + Arrays.toString(lectureNames));

        Log.v(TAG, "FILE path = " + files[0].getAbsolutePath());
        if (!files[0].exists() || !files[0].canRead()) {
            Log.v(TAG, "Couldn't find/read file.");
            return null;
        }

        ArrayList<Uri> uris = new ArrayList<Uri>();
        for(File file:files)
            uris.add(FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file));

        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        return emailIntent;
    }

}
