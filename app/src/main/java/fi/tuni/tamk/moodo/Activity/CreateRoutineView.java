package fi.tuni.tamk.moodo.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.tamk.moodo.R;

public class CreateRoutineView extends AppCompatActivity {

    ArrayList<String> listItems = new ArrayList<String>();
    ListView listView = findViewById(R.id.create_routine_listview);
    ArrayAdapter<String> adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_routine_view);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);
    }

    // insert data into list
    public void addItems(View v) {
        listItems.add("Test");
        adapter.notifyDataSetChanged();
    }
}
