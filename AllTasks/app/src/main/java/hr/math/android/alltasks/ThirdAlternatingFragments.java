package hr.math.android.alltasks;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class ThirdAlternatingFragments extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_alternating_fragments);

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		//---get the current display info---
		WindowManager wm = getWindowManager();
		Display d = wm.getDefaultDisplay();
		if ((d.getRotation()== Surface.ROTATION_90) || (d.getRotation()==Surface.ROTATION_270))
		{
			//---landscape mode---
			ThirdFragment1 thirdFragment1 = new ThirdFragment1();
			// android.R.id.content refers to the content
			// view of the activity
            fragmentTransaction.replace(
                    android.R.id.content, thirdFragment1);
        }
		else
		{
			//---portrait mode---
			ThirdFragment2 thirdFragment2 = new ThirdFragment2();
			fragmentTransaction.replace(
					android.R.id.content, thirdFragment2);
		}
        //---add to the back stack---
        fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();


    }

	@Override
	public void onBackPressed()
	{
		Intent intent = new Intent(this, ThirdClass.class);
		startActivity(intent);
	}
}
