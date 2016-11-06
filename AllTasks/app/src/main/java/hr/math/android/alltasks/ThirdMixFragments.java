package hr.math.android.alltasks;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

public class ThirdMixFragments extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_mix_fragments);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //---get the current display info---
        WindowManager wm = getWindowManager();
        Display d = wm.getDefaultDisplay();
        if ((d.getRotation()== Surface.ROTATION_90) || (d.getRotation()==Surface.ROTATION_270))
        {
            //---landscape mode---
            ThirdFragment1 thirdFragment1 = new ThirdFragment1();
            ThirdFragment2 thirdFragment2 = new ThirdFragment2();
            // android.R.id.content refers to the content
            // view of the activity
            fragmentTransaction.replace(android.R.id.content, thirdFragment2);
            fragmentTransaction.add(R.id.third_fragment2, thirdFragment1);
        }
        else
        {
            //---portrait mode---
            ThirdFragment1 thirdFragment1 = new ThirdFragment1();
            fragmentTransaction.replace(
                    android.R.id.content, thirdFragment1);
        }
        //---add to the back stack---
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    public void startFragment2(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();

        ThirdFragment2 thirdFragment2 = new ThirdFragment2();
        fragmentTransaction.replace(
                android.R.id.content, thirdFragment2);

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
