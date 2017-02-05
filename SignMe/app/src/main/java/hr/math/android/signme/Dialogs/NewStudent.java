package hr.math.android.signme.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import hr.math.android.signme.Database.DBStudents;
import hr.math.android.signme.DrawingFragment;
import hr.math.android.signme.R;

/**
 * Created by ivan on 04.02.17..
 */

public class NewStudent {

    private static final String TAG = "POP_NEW_STUDENT";

    public static void popUpNewStudents(final Activity activity, final int lectureId, final DBStudents db) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.new_student);

        final LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputName = new EditText(activity);
        inputName.setHint(R.string.add_name);
        inputName.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(inputName);

        final EditText inputSurname = new EditText(activity);
        inputSurname.setHint(R.string.add_surname);
        inputSurname.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(inputSurname);

        final EditText inputJmbag = new EditText(activity);
        inputJmbag.setHint(R.string.add_jmbag);
        inputJmbag.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(inputJmbag);

        builder.setView(layout);

        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int new_studentId = addNewStudent(inputName.getText().toString(), inputSurname.getText().toString(),
                        inputJmbag.getText().toString(), db, lectureId, activity);
                if(new_studentId != -1)
                    startSigningScreen(true, new_studentId, activity, lectureId);
                else
                    badStudentInfo(inputName.getText().toString(), inputSurname.getText().toString(),
                            inputJmbag.getText().toString(), activity);
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

    private static void badStudentInfo(String name, String surname, String jmbag, Activity activity)
    {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        Snackbar.make(getView(), "Given student info is not valid.\nName = " + name + "; Surname = "
//                + surname + "; JMBAG = " + jmbag, Snackbar.LENGTH_LONG).show();
        Toast.makeText(activity, "Given student info is not valid.\nName = " + name + "; Surname = "
                + surname + "; JMBAG = " + jmbag, Toast.LENGTH_LONG).show();
    }

    private static void startSigningScreen(boolean learningNew, int studentId, Activity activity, int lectureId)
    {
        Toast.makeText(activity, "Starting fragment for fragment_drawing", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Starting fragment for fragment_drawing");
        FragmentTransaction ft = ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, DrawingFragment.newInstance(learningNew, studentId, lectureId));
        ft.commit();
    }

    private static int addNewStudent(String name, String surname, String JMBAG, DBStudents db, int lectureId, Activity activity)
    {
        int jmbag;
        if(name.trim().length() == 0) {
            Log.d(TAG, "Given new student name = " + name + ", necessary student name");
            Toast.makeText(activity, "Student name is mandatory", Toast.LENGTH_LONG).show();
            return -1;
        }
        if(surname.trim().length() == 0) {
            Log.d(TAG, "Given new student surname = " + surname + ", necessary student surname");
            Toast.makeText(activity, "Student surname is mandatory", Toast.LENGTH_LONG).show();
            return -1;
        }
        try{
            jmbag = Integer.parseInt(JMBAG);
        }
        catch(NumberFormatException e){
            Log.d(TAG, "Given new student JMBAG = " + JMBAG + ", necessary true number");
            Toast.makeText(activity, "JMBAG number is mandatory", Toast.LENGTH_LONG).show();
            return -1;
        }

        if (!db.doesStudentExist(lectureId, jmbag)) {
            db.newStudent(name, surname, jmbag, lectureId);
            Toast.makeText(activity, "Added new student " + name, Toast.LENGTH_SHORT).show();
            return db.getStudentID(lectureId, jmbag);

        }
        else
            Toast.makeText(activity, "Student with JMBAG " + JMBAG + " already exist.", Toast.LENGTH_SHORT).show();
        return -1;
    }


}
