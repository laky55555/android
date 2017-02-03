package hr.math.android.signme;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by mira on 14.01.17..
 */

public class SendMail {

    private static String TAG = "Send Mail";
    private static int startRow = 2;
    private static int startColumn = 3;


    private static boolean fillTable(WritableSheet sheet, Context context, int lectureId) {
        DBAttendance attendance = new DBAttendance(context);
        attendance.open();
        Cursor students = attendance.getAllStudentsOfLecture(lectureId);
        Cursor dates = attendance.getAllDatesOfLecture(lectureId);
        if(students == null || dates == null)
            return false;
        int student_ids[] = new int[students.getCount()];
        String dateList[] = new String[dates.getCount()];
        Log.v(TAG, "Number of students = " + students.getCount());
        Log.v(TAG, "Number of dates = " + dates.getCount());
        try {
            int row = startRow;
            if (students.moveToFirst()) {
                do {
                    Log.v(TAG, students.getString(0) + " " + students.getString(1) + " " + students.getString(2) + ", row " + row);
                    sheet.addCell(new Label(0, row, students.getString(0))); // column and row
                    sheet.addCell(new Label(1, row, students.getString(1))); // column and row
                    sheet.addCell(new Label(2, row, students.getString(2))); // column and row
                    student_ids[row - startRow] = students.getInt(3);
                    row++;
                } while (students.moveToNext());
            }

            int column = startColumn;
            if (dates.moveToFirst()) {
                do {
                    Log.v(TAG, dates.getString(0) + " column " + column);
                    sheet.addCell(new Label(column, startRow-1, dates.getString(0))); // column and row
                    dateList[column - startColumn] = dates.getString(0);
                    column++;
                } while (dates.moveToNext());
            }

            for (int i=0; i<student_ids.length; i++) {
                for (int j=0; j<dateList.length; j++) {
                    String answer = attendance.hasAttended(student_ids[i], lectureId, dateList[j]);
                    Log.v(TAG, "student " + student_ids[i] + " on date" + dateList[j] + " answered " + answer);
                    sheet.addCell(new Label(startColumn + j, startRow + i, answer));
                }
            }
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

        attendance.close();
        return true;
    }

    //TODO: now is working just for one lecture, need to change so it can work for array
    private static void exportToExcel(Context context, int[] lectureIds) {
        final String fileName = "TodoList.xls";

        //Saving file in external storage
        java.io.File sdCard = Environment.getExternalStorageDirectory();
        java.io.File directory;
        try{
//            directory = new java.io.File("excelTablica");
            directory = new java.io.File(sdCard.getAbsolutePath() + "/excelTablica");
            if(!directory.isDirectory()){
                directory.mkdirs();
            }

            java.io.File file = new java.io.File(directory, fileName);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;

            try {
                workbook = Workbook.createWorkbook(file, wbSettings);
                //Excel sheet name. 0 represents first sheet
                WritableSheet sheet = workbook.createSheet("MyShoppingList", 0);
                fillTable(sheet, context, lectureIds[0]);
                workbook.write();
                try {
                    workbook.close();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        catch (Exception e)
        {
            Log.d("exception", "D" + e);
        }

    }

    public static Intent sendMail(Context context, int[] lectureIds){

        exportToExcel(context, lectureIds);

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"vita89jela@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "pisem ti pisem");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "ne volim te vise");
        java.io.File root = Environment.getExternalStorageDirectory();
        String pathToMyAttachedFile = "excelTablica/TodoList.xls";
        java.io.File file = new java.io.File(root, pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            return null;
        }
        Uri uri = Uri.fromFile(file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return  emailIntent;
    }

}
