package hr.math.android.signme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Limbo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limbo);
    }

    public void exitPopUp(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.password_for_exit);

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText input_password = new EditText(this);
        input_password.setHint(R.string.enter_password);
        input_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        layout.addView(input_password);

        builder.setView(layout);

        builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkPassword(input_password.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void checkPassword(String inputPassword) {
        String storedPass = new Password(this).getPassword();
        if(storedPass.equals(inputPassword)){
            Intent intent = new Intent(this, MainActivity.class);
            getPackageManager().clearPackagePreferredActivities(getPackageName());
            startActivity(intent);
            finish();
        }
        else
            Toast.makeText(this, "Krivi pass, tocni pass = " + storedPass, Toast.LENGTH_SHORT).show();
    }

}
