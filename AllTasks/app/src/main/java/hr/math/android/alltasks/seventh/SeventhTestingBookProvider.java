package hr.math.android.alltasks.seventh;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hr.math.android.alltasks.R;

public class SeventhTestingBookProvider extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seventh_testing_book_provider);
    }


    public void onClickDeleteBook(View view) {

        //ContentValues values = new ContentValues();
        //values.put("isbn", ((EditText)findViewById(R.id.txtISBN)).getText().toString());
//        Uri uri = getContentResolver().insert(
//                Uri.parse(
//                        "content://hr.math.android.alltasks.seventh.contprov/books"),
//                values);
        Uri uri = Uri.parse("content://hr.math.android.alltasks.seventh.contprov/books");
        String isbn = ((EditText)findViewById(R.id.txtISBN)).getText().toString();
        int deleted = getContentResolver().delete(uri, "isbn=?", new String[]{isbn});
        Toast.makeText(this, "Deleted " + deleted + " books.", Toast.LENGTH_SHORT).show();
    }

    public void onClickAddTitle(View view) {

        //---add a book---

        ContentValues values = new ContentValues();
        values.put("title", ((EditText)
                findViewById(R.id.txtTitle)).getText().toString());
        values.put("isbn", ((EditText)
                findViewById(R.id.txtISBN)).getText().toString());
        Uri uri = getContentResolver().insert(
                Uri.parse(
                        "content://hr.math.android.alltasks.seventh.contprov/books"),
                values);
    }

    public void onClickRetrieveTitles(View view) {
        //---retrieve the titles---
        Uri allTitles = Uri.parse(
                "content://hr.math.android.alltasks.seventh.contprov/books");

        Cursor c;

        if (android.os.Build.VERSION.SDK_INT <11) {
            //---before Honeycomb---
            c = managedQuery(allTitles, null, null, null,
                    "title desc");
        } else {
            //---Honeycomb and later---
            CursorLoader cursorLoader = new CursorLoader(
                    this,
                    allTitles, null, null, null,
                    "title desc");
            c = cursorLoader.loadInBackground();
        }

        if (c.moveToFirst()) {
            do{
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(
                                BookProvider._ID)) + ", " +
                                c.getString(c.getColumnIndex(
                                        BookProvider.TITLE)) + ", " +
                                c.getString(c.getColumnIndex(
                                        BookProvider.ISBN)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
    }

    public void onClickRetrieveTitlesFromBL(View view) {
        //---retrieve the titles---
        Uri allTitles = Uri.parse(
                "content://hr.math.android.alltasks.seventh.contprov/books/bl");

        Cursor c;
        if (android.os.Build.VERSION.SDK_INT <11) {
            //---before Honeycomb---
            c = managedQuery(allTitles, null, null, null,
                    "title desc");
        } else {
            //---Honeycomb and later---
            CursorLoader cursorLoader = new CursorLoader(
                    this,
                    allTitles, null, null, null,
                    "title desc");
            c = cursorLoader.loadInBackground();
        }

        if (c.moveToFirst()) {
            do{
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(
                                BookProvider._ID)) + ", " +
                                c.getString(c.getColumnIndex(
                                        BookProvider.TITLE)) + ", " +
                                c.getString(c.getColumnIndex(
                                        BookProvider.ISBN)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
    }
}
