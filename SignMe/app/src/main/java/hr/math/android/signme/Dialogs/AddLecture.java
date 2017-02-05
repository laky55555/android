package hr.math.android.signme.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import hr.math.android.signme.Database.DBLectures;
import hr.math.android.signme.R;

/**
 * Created by ivan on 04.02.17..
 */

public class AddLecture {

    private static class CustomListener implements View.OnClickListener {
        private Dialog dialog;
        private DBLectures db;
        private EditText editText;
        private CursorAdapter adapter;
        private Activity activity;
        private Cursor cursor;
        CustomListener(Dialog dialog, DBLectures db, EditText editText, CursorAdapter adapter, Activity activity, Cursor cursor) {
            this.dialog = dialog;
            this.db = db;
            this.editText = editText;
            this.adapter = adapter;
            this.activity = activity;
            this.cursor = cursor;
        }
        @Override
        public void onClick(View v) {
            String mValue = editText.getText().toString();

            if(mValue.length() != 0){
                addNewLecture(mValue, db, activity, cursor, adapter);
                dialog.dismiss();
            }else{
                Toast.makeText(activity, "Invalid data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void popUpAdd(final DBLectures db, final CursorAdapter adapter, final Activity activity, final Cursor cursor)
    {
        Toast.makeText(activity, "POP up add", Toast.LENGTH_SHORT);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.add_lecture);

        final LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputName = new EditText(activity);
        inputName.setHint(R.string.add_lecture);
        inputName.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(inputName);

        builder.setView(layout);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                addNewLecture(inputName.getText().toString(), db, activity, cursor, adapter);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialog, db, inputName, adapter, activity, cursor));

    }

    private static void addNewLecture(String name, DBLectures db, Activity activity, Cursor cursor, CursorAdapter adapter) {

        if (!db.doesLectureExist(name)) {
            db.newLecture(name);
            Toast.makeText(activity, "Added new lecture", Toast.LENGTH_SHORT).show();
            cursor.requery();
            adapter.notifyDataSetChanged();
        } else
            Toast.makeText(activity, "Lecture with " + name + " already exist.", Toast.LENGTH_SHORT).show();
    }


}
