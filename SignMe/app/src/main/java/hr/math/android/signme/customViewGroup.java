package hr.math.android.signme;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by ivan on 17.01.17..
 * ViewGroup responsible to overwriting status bar so user couldn't open it.
 */

public class customViewGroup extends ViewGroup {

    public customViewGroup(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.v("customViewGroup", "**********Intercepted");
        return true;
    }
}