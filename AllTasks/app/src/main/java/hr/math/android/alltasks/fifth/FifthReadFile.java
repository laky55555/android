package hr.math.android.alltasks.fifth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import hr.math.android.alltasks.R;

public class FifthReadFile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_read_file);

        InputStream is=this.getResources().openRawResource(R.raw.some_text);
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String str=null;
        try{
            while((str=br.readLine())!=null){
                Toast.makeText(getBaseContext(),str,Toast.LENGTH_LONG).show();
            }
            is.close();
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
