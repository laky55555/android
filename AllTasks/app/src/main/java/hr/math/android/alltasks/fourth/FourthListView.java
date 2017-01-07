package hr.math.android.alltasks.fourth;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import hr.math.android.alltasks.R;

public class FourthListView extends ListActivity {
    String[] kolegiji;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView prviView = getListView();
        prviView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        prviView.setTextFilterEnabled(true);
        kolegiji = getResources().getStringArray(R.array.kolegiji);

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, kolegiji));
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
        Toast.makeText(this, " you choice is " + kolegiji[position], Toast.LENGTH_SHORT).show();
    }
}

