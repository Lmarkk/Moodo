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

        addDefaultSubRoutines();

        // Add list of routines to the list view
        ArrayAdapter<Routine> adapter = new ArrayAdapter<>(this, R.layout.list_item, routineList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Routine listItem = (Routine) listView.getItemAtPosition(position);
                Log.d("MoodoApp", "Subroutines: " + listItem.getSubRoutines().toString());
                Intent intent = new Intent(getApplicationContext(), RoutineView.class);
                // Add clicked list routine to be displayed in RoutineView
                intent.putExtra("routine", listItem);
                // Toast.makeText(getApplicationContext(), listItem.toString(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    // Adds default routines for all (unless deleted if possible)
    private void addDefaultRoutines() {
        routineList.add(new Routine(1,"Pese hampaat", 300));
        routineList.add(new Routine(2,"Vie roskat", 300));
    }

    // Adds sub routines for default routines using routine id's
    private void addDefaultSubRoutines() {
        ArrayList<SubRoutine> subRoutines = new ArrayList<>();
        for(Routine item : routineList) {
            System.out.println("Itemin id: " + item.getId());
            switch (item.getId()) {
                case 1:
                    subRoutines.add(new SubRoutine(1, "Ota hammasharja"));
                    subRoutines.add(new SubRoutine(2, "Lisää hammastahna"));
                    subRoutines.add(new SubRoutine(3, "Pese hampaita kolme minuuttia"));
                    subRoutines.add(new SubRoutine(4, "Huuhtele suu ja hammasharja"));
                    break;
                case 2:
                    subRoutines.add(new SubRoutine(1, "Sulje roskapussi"));
                    subRoutines.add(new SubRoutine(2, "Nosta roskapussi pois säiliöstä"));
                    subRoutines.add(new SubRoutine(3, "Vie roskat roskikseen"));
                    subRoutines.add(new SubRoutine(4, "Laita uusi roskapussi tilalle"));
            }
            if(subRoutines.size() != 0) {
                Log.d("MoodoApp", subRoutines.toString());
                item.setSubRoutines(subRoutines);
                subRoutines.clear();
            }
        }
    }

    // Adds custom routines for each user
    private void addCustomRoutines() {

    }
}
