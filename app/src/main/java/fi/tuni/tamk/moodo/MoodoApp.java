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
    private ListView listView;
    private ArrayList<Routine> routineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MoodoApp", "onCreate");
        setContentView(R.layout.moodo_app);

        // Create view and add routines to list
        listView = findViewById(R.id.routine_list);
        routineList = new ArrayList<>();
        // Add the default routines for all
        addDefaultRoutines();
        // Adds custom routines that user has made
        // addCustomRoutines();

        // Add list of routines to the list view
        ArrayAdapter<Routine> adapter = new ArrayAdapter<>(this, R.layout.list_item, routineList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Routine listItem = (Routine) listView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), RoutineView.class);
                // Add clicked list routine to be displayed in RoutineView
                intent.putExtra("routine", listItem);
                Toast.makeText(getApplicationContext(), listItem.toString(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    // Adds default routines for all (unless deleted if possible)
    private void addDefaultRoutines() {
        routineList.add(new Routine("Pese hampaat"));
        routineList.add(new Routine("Vie roskat"));
    }

    // Adds custom routines for each user
    private void addCustomRoutines() {

    }
}
