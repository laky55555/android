package hr.math.android.signme.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hr.math.android.signme.Database.DBLectures;
import hr.math.android.signme.R;

/**
 * Created by ivan on 04.02.17..
 */

public class DeleteLecture {

    public static void popUpDelete(final ArrayList<String> forDelete, final DBLectures db, final CursorAdapter adapter, Activity activity, final Cursor cursor) {
        if(forDelete.size() < 1)
            return;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.deleting_marked_lectures);

        final TextView info = new TextView(activity);
        info.setText(activity.getString(R.string.delete_warning) + ":\n" + forDelete.toString());
        builder.setView(info);

        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < forDelete.size(); ++i)
                    db.deleteLecture(forDelete.get(i));
                cursor.requery();
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
