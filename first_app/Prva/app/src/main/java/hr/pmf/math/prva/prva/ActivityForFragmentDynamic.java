package hr.pmf.math.prva.prva;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public class ActivityForFragmentDynamic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_for_fragment_dynamic);


		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction =
				fragmentManager.beginTransaction();

		//---get the current display info---
		WindowManager wm = getWindowManager();
		Display d = wm.getDefaultDisplay();
		if ((d.getRotation()==0)||(d.getRotation()==2))
		{
			//---landscape mode---
			Fragment1 fragment1 = new Fragment1();
			// android.R.id.content refers to the content
			// view of the activity
			fragmentTransaction.replace(
					android.R.id.content, fragment1);
		}
		else
		{
            Fragment1 fragment1 = new Fragment1();
            fragmentTransaction.replace(
                    android.R.id.content, fragment1);

			Fragment2 fragment2 = new Fragment2();

            fragmentTransaction.add(
                    R.id.frag1, fragment2);
		}
        //---add to the back stack---
        fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();

        Log.v("myTag", "This is my message");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_for_fragment_dynamic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startFragment2(View view){

        Log.v("myTag", "This is my message       111");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();

        //---get the current display info---
        //WindowManager wm = getWindowManager();
        //Display d = wm.getDefaultDisplay();

        Log.v("myTag", "This is my message  2222");

        Fragment2 fragment2 = new Fragment2();
        fragmentTransaction.replace(
                android.R.id.content, fragment2);
        Log.d("myTag", "This is my message    333333 ");

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        Log.d("myTag", "This is my message       4444444");

    }
}
