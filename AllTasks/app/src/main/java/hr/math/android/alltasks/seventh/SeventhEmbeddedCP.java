package hr.math.android.alltasks.seventh;

import android.Manifest;
import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import hr.math.android.alltasks.R;

public class SeventhEmbeddedCP extends ListActivity {

    private int MY_PERM=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seventh_embedded_cp);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERM);
        }
        else {
            //Uri allContacts = Uri.parse("content://contacts/people");
            Uri allContacts = ContactsContract.Contacts.CONTENT_URI;


            Cursor c;
            if (android.os.Build.VERSION.SDK_INT < 11) {
                //---before Honeycomb---
                c = managedQuery(allContacts, null, null, null, null);

            } else {
                //---Honeycomb and later---
                CursorLoader cursorLoader = new CursorLoader(
                        this,
                        allContacts,
                        null,
                        null,
                        null,
                        null);
                c = cursorLoader.loadInBackground();
            }

            String[] columns = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER};

            int[] views = new int[]{R.id.contactName, R.id.contactID, R.id.contactNumber};

            SimpleCursorAdapter adapter;

            if (android.os.Build.VERSION.SDK_INT < 11) {
                //---before Honeycomb---
                adapter = new SimpleCursorAdapter(
                        this, R.layout.activity_seventh_embedded_cp, c, columns, views);
            } else {
                //---Honeycomb and later---
                adapter = new SimpleCursorAdapter(
                        this, R.layout.activity_seventh_embedded_cp, c, columns, views,
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            }
            this.setListAdapter(adapter);
        }
    }
}
