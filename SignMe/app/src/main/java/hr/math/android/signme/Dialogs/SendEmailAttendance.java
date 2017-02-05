package hr.math.android.signme.Dialogs;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import hr.math.android.signme.Database.DBLectures;
import hr.math.android.signme.R;
import hr.math.android.signme.SendMail;

/**
 * Created by ivan on 04.02.17..
 */

public class SendEmailAttendance {

    public static void popUpSend(final ArrayList<String> forSend, Context context, View view)
    {
        if (forSend.size() < 1)
            return;

        int lectureIds[] = new int[forSend.size()];
        String lectureNames[] = new String[forSend.size()];
        DBLectures lectures = new DBLectures(context);
        lectures.open();
        for(int i=0; i<lectureIds.length; i++) {
            lectureIds[i] = lectures.getLectureID(forSend.get(i));
            lectureNames[i] = forSend.get(i);
        }
        lectures.close();

        Log.v("NESTO", Arrays.toString(lectureIds));
        Intent i = SendMail.sendMail(context, lectureIds, lectureNames);

        if(i != null)
            context.startActivity(i);
        else
            Snackbar.make(view, R.string.unsuccessful_mail, Snackbar.LENGTH_LONG).show();
    }

}
