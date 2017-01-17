package hr.math.android.signme;

import android.content.Intent;
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

    //TODO: napraviti slanje mailova!!!!!!!!!!!
    private static void exportToExcel() {
        final String fileName = "TodoList.xls";

        //Saving file in external storage
        java.io.File sdCard = Environment.getExternalStorageDirectory();
        java.io.File directory;
        try{
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

                try {
                    sheet.addCell(new Label(2, 1, "murac")); // column and row
                    sheet.addCell(new Label(3, 1, "laky"));
                    for(int i = 1; i < 10; ++i) {
                        sheet.addCell(new Label(1, i+1, "13.1.2017."));
                        sheet.addCell(new Label(2, i+1, "+"));
                        sheet.addCell(new Label(3, i+1, "-"));
                    }
                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
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
            Log.d("exception", "D");
        }

    }

    public static Intent sendMail(){

        exportToExcel();

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
