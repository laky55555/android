package hr.math.android.alltasks.fifth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import hr.math.android.alltasks.R;

public class FifthLogDebug extends AppCompatActivity {

    public final static String TAG1 = "SOME_TAG";
    public final static String TAG2 = "MORE_TAGS";
    public final static String TAG3 = "TRUBE";

    private final static String message = "There're few Log methods that you can use with following priorities.\n" +
            "Log.v -> verbose, value 2\nLog.d -> debug, value 3\nLog.i -> info, value 4\n" +
            "Log.w -> warning, value 5\nLog.e -> error, value 6\nLog.wtf -> what a terrible failure, value 7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_log_debug);
    }

    public void logI(View view) {
        try {
            throw new IllegalArgumentException();
        }
        catch (Exception e) {
            Log.e (TAG1, "log.i is for information.", e);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void logE(View view) {
        try {
            throw new ClassNotFoundException();
        }
        catch (Exception e) {
            Log.e (TAG2, "Log.e is for error.", e);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void logWTF(View view) {
        try {
            throw new NoSuchMethodException();
        }
        catch (Exception e) {
            Log.wtf (TAG3, "Log.wtf doesn't stand for what a fuck, but for \"what a terrible failure\". ", e);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
