package hr.math.android.alltasks.sixth;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import hr.math.android.alltasks.R;

public class SixthClass extends AppCompatActivity {

    EditText editName;
    EditText editStreet;
    EditText editNumber;

    private  DBAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sixth_class);

        createFieldForInput();

        db = new DBAdapter(this);


        //---add a contact---
//        db.open();
//        long id = db.insertContact("Wei-Meng Lee", "weimenglee@learn2develop.net");
//        id = db.insertContact("Mary Jackson", "mary@jackson.com");
//        db.close();



        //--get all contacts---
        db.open();
        Cursor c = db.getAllContacts();
        if (c.moveToFirst())
        {
            do {
                DisplayContact(c);
            } while (c.moveToNext());
        }
        db.close();



        //---get a contact---
        db.open();
        Cursor cu = db.getContact(2);
        if (cu.moveToFirst())
            DisplayContact(cu);
        else
            Toast.makeText(this, "No contact found", Toast.LENGTH_LONG).show();
        db.close();



        //---update contact---
        db.open();
        if (db.updateContact(3, "Wei-Meng Lee", "weimenglee@gmail.com"))
            Toast.makeText(this, "Update successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();
        db.close();



        //---delete a contact---
//        db.open();
//        if (db.deleteContact(1))
//            Toast.makeText(this, "Delete successful.", Toast.LENGTH_LONG).show();
//        else
//            Toast.makeText(this, "Delete failed.", Toast.LENGTH_LONG).show();
//        db.close();


    }

    private void createFieldForInput() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        // Creating a new TextView
        editName = new EditText(this);
        editStreet = new EditText(this);
        editNumber = new EditText(this);
        Button confirm = new Button(this);
        Button display = new Button(this);
        editName.setHint("Contact name");
        editStreet.setHint("Street name");
        editNumber.setHint("Street number");
        editNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        confirm.setText(getResources().getText(R.string.save));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAdress(v);
            }
        });
        display.setText(getResources().getText(R.string.load));
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllAddresses(v);
            }
        });

        linearLayout.addView(editName);
        linearLayout.addView(editStreet);
        linearLayout.addView(editNumber);
        linearLayout.addView(confirm);
        linearLayout.addView(display);

        setContentView(linearLayout);
    }

    public void DisplayContact(Cursor c)
    {
        Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                        "Name: " + c.getString(1) + "\n" +
                        "Email:  " + c.getString(2),
                Toast.LENGTH_SHORT).show();
    }

    private void getAllAddresses(View view) {
        db.open();
        Cursor c = db.getAllAddresses();
        if (c.moveToFirst())
        {
            do {
                DisplayAddress(c);
            } while (c.moveToNext());
        }
        db.close();

    }

    public void DisplayAddress(Cursor c)
    {
        Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                        "Name: " + c.getString(1) + "\n" +
                        "Street: " + c.getString(2) + "\n" +
                        "Number:  " + c.getString(3),
                Toast.LENGTH_LONG).show();
    }

    public void addAdress(View view) {
        db.open();
        Toast.makeText(this, "name =" + editName.getText() + ".", Toast.LENGTH_LONG).show();
        if(editName.getText().length() > 0 && editStreet.getText().length() > 0 &&
                editNumber.getText().length() > 0) {
            long id = db.insertAddress(editName.getText().toString(), editStreet.getText().toString(),
                    Integer.parseInt(editNumber.getText().toString()));
        }
        db.close();
    }


}
