package hr.math.android.alltasks.fourth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import hr.math.android.alltasks.R;

public class FourthViewElements extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_view_elements);
    }
    public void btnSaved_clicked(View view){
        Toast.makeText(this, "Save clicked", Toast.LENGTH_LONG).show();
    }

    private void DisplayToast(String msg){
        Toast.makeText(getBaseContext(),msg, Toast.LENGTH_SHORT).show();
    }

    public void checkboxClicked(View view) {
        if (((CheckBox)view).isChecked())
            DisplayToast("CheckBox selected");
        else
            DisplayToast("CheckBox not selected");
    }

    public void radioBoxHoovered(View view) {
        if(((RadioGroup)view).isHovered())
            DisplayToast("Radio group is pressed");
    }



}
