package hr.math.android.alltasks.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hr.math.android.alltasks.MainActivity;
import hr.math.android.alltasks.R;

public class EnterPassword extends AppCompatActivity {

    private String password = "Murac";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
    }

    public void checkPassword(View view) {
        String pass = ((EditText) findViewById(R.id.password)).getText().toString();
        if(pass.equals(password)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "Krivi pass", Toast.LENGTH_SHORT).show();
        finish();
    }
}
