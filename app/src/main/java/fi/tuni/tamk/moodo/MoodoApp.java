package fi.tuni.tamk.moodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MoodoApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moodo_app);
        ListView listView = findViewById(R.id.routine_list);
        ArrayList<Routine> list = new ArrayList<>();
        list.add(new Routine("Pese hampaat"));
        list.add(new Routine("Vie roskat"));
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, list);
        listView.setAdapter(adapter);
        // listView.setOnItemClickListener((parent, view1, position, id) -> callback.itemSelected(list.get(position)));
    }
}
