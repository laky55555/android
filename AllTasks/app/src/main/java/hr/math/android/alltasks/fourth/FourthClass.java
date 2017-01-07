package hr.math.android.alltasks.fourth;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import hr.math.android.alltasks.R;
import hr.math.android.alltasks.second.SecondWindowLikeDialog;

public class FourthClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_class);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ID", "1234567890");
    }

    //Za kasniji dohvat informacije
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String ID = savedInstanceState.getString("ID");
        Toast.makeText(this, ID, Toast.LENGTH_LONG).show();
    }


    public void startLinearLayout(View view) {
        Intent intent = new Intent(this, FourthLinearLayout.class);
        startActivity(intent);
    }

    public void startRelativeLayout(View view) {
        setContentView(R.layout.activity_fourth_relative_layout);
    }

    public void startTableLayout(View view) {
        setContentView(R.layout.activity_fourth_table_layout);
    }

    public void startFrameLayout(View view) {
        setContentView(R.layout.activity_fourth_frame_layout);
    }

    public void startAnchoring(View view) {
        setContentView(R.layout.activity_fourth_anchoring);
    }

    public void startViewElements(View view){
        Intent intent = new Intent(this, FourthViewElements.class);
        startActivity(intent);
    }

    public void startProgresBar(View view){
        Intent intent = new Intent(this, FourthProgressBar.class);
        startActivity(intent);
    }

    public void startListView(View view) {
        Intent intent = new Intent(this, FourthListView.class);
        startActivity(intent);
    }

    public void startGallery(View view) {
        Intent intent = new Intent(this, FourthGalery.class);
        startActivity(intent);
    }

}
