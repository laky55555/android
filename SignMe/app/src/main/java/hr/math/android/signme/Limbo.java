package hr.math.android.signme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import hr.math.android.signme.Dialogs.CheckPassword;


//TODO: vidjeti da li tu uopce mozemo ikad doci osim klikom na home i pocetnog ekrana??
public class Limbo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limbo);
    }

    public void exitLimbo() {
        Intent intent = new Intent(this, MainActivity.class);
        getPackageManager().clearPackagePreferredActivities(getPackageName());
        startActivity(intent);
        finish();
    }

    public void exitPopUp(View view) {
        CheckPassword.popUpPassword(getBaseContext(), this);
    }

}
