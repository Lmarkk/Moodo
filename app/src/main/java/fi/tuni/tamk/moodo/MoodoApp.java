package fi.tuni.tamk.moodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MoodoApp extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MoodoApp", "onCreate");
        setContentView(R.layout.moodo_app);

        listView = findViewById(R.id.routine_list);
        ArrayList<Routine> list = new ArrayList<>();
        list.add(new Routine("Pese hampaat"));
        list.add(new Routine("Vie roskat"));

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Routine listItem = (Routine) listView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), RoutineView.class);
                intent.putExtra("routine", listItem);
                Toast.makeText(getApplicationContext(), listItem.toString(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}
